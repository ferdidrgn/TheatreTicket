package com.ferdidrgn.theatreticket.presentation.editProfile

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.util.base.BaseViewModel
import com.ferdidrgn.theatreticket.domain.model.commonModels.dummyData.User
import com.ferdidrgn.theatreticket.util.ID
import com.ferdidrgn.theatreticket.util.Response
import com.ferdidrgn.theatreticket.util.Roles
import com.ferdidrgn.theatreticket.util.WhichEditProfile
import com.ferdidrgn.theatreticket.data.repository.UserFirebaseQueries
import com.ferdidrgn.theatreticket.util.ClientPreferences
import com.ferdidrgn.theatreticket.util.helpers.LiveEvent
import com.ferdidrgn.theatreticket.util.isEmailValid
import com.ferdidrgn.theatreticket.util.mainScope
import com.ferdidrgn.theatreticket.util.removeWhiteSpace
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.*
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(private val userFirebaseQueries: UserFirebaseQueries) :
    BaseViewModel() {

    val fullName = MutableStateFlow("")
    var firstName = MutableStateFlow("")
    val lastName = MutableStateFlow("")
    val phoneNumber = MutableStateFlow("")
    val age = MutableStateFlow("")
    val imageUrl = MutableStateFlow("")
    val addOrUpdateImgUrl = MutableStateFlow<Uri?>(null)
    val eMail = MutableStateFlow("")
    val toolbarText = MutableStateFlow("")
    val userId = ClientPreferences.inst.userID
    val role = LiveEvent<String?>()

    val whichComeAction = MutableStateFlow(WhichEditProfile.LogIn)
    var isAddUserActions = false
    var phoneNumberClickable = false
    var fullNameClickable = false
    val btnCstmDatePickerClick = LiveEvent<Boolean?>()
    val btnUpdateAccClick = LiveEvent<Boolean?>()
    val btnDeleteAccountClick = LiveEvent<Boolean?>()
    val imagePermission = MutableLiveData<Boolean?>()

    val logOut = LiveEvent<Boolean?>()


    init {
        getUser()
        phoneNumberClickable = ClientPreferences.inst.isPhoneNumberSignIn == false
        fullNameClickable = ClientPreferences.inst.isGoogleSignIn == false
    }

    fun changeToolbarText() {
        when (whichComeAction.value) {
            WhichEditProfile.LogIn -> {
                toolbarText.value = message(R.string.edit_profile)
                isAddUserActions = (false)
            }
            WhichEditProfile.Settings -> {
                toolbarText.value = message(R.string.add_user_info)
                isAddUserActions = (true)
            }
        }
    }

    private fun getUser() {
        mainScope {
            showLoading()
            if (ClientPreferences.inst.reviewStatus.not()) {
                if (ClientPreferences.inst.reviewCounter % 3 == 0) {
                    userFirebaseQueries.checkUserId(userId) { status, userInfoList ->
                        when (status) {
                            Response.Empty -> {
                                ClientPreferences.inst.apply {
                                    fullName.value = userFullName.toString()
                                    firstName.value = userFirstName.toString()
                                    lastName.value = userLastName.toString()
                                    phoneNumber.value = userPhone?.removeWhiteSpace().toString()
                                    age.value = userAge.toString()
                                    imageUrl.value = userPhotoUrl.toString()
                                    eMail.value = userEmail.toString()
                                }
                                hideLoading()
                            }
                            Response.ThereIs -> {
                                ClientPreferences.inst.apply {
                                    role = userInfoList?.role.toString()
                                    userID = userInfoList?._id
                                    userFullName = userInfoList?.fullName
                                    userEmail = userInfoList?.eMail
                                    userPhone = userInfoList?.phoneNumber
                                    userPhotoUrl = userInfoList?.imgUrl.toString()
                                    token = userInfoList?.token
                                    FCMtoken = userInfoList?.fcmToken
                                    this.userFirstName = userInfoList?.firstName
                                    this.userLastName = userInfoList?.lastName
                                    this.userAge = userInfoList?.age
                                }

                                fullName.value = userInfoList?.fullName.toString()
                                firstName.value = userInfoList?.firstName.toString()
                                lastName.value = userInfoList?.lastName.toString()
                                phoneNumber.value =
                                    userInfoList?.phoneNumber?.removeWhiteSpace().toString()
                                age.value = userInfoList?.age.toString()
                                imageUrl.value = userInfoList?.imgUrl.toString()
                                eMail.value = userInfoList?.eMail.toString()

                                hideLoading()
                            }
                            Response.NotEqual -> {
                                hideLoading()
                                errorMessage.postValue(message(R.string.error_not_equal))
                            }
                            Response.ServerError -> {
                                hideLoading()
                                errorMessage.postValue(message(R.string.error_server))
                            }
                        }
                    }
                } else {
                    ClientPreferences.inst.apply {
                        fullName.value = userFullName.toString()
                        firstName.value = userFirstName.toString()
                        lastName.value = userLastName.toString()
                        phoneNumber.value = userPhone?.removeWhiteSpace().toString()
                        age.value = userAge.toString()
                        imageUrl.value = userPhotoUrl.toString()
                        eMail.value = userEmail.toString()
                    }
                    hideLoading()
                }
            }
        }
    }

    private fun updateOrAddProfile() {
        mainScope {
            showLoading()

            val id = if (ClientPreferences.inst.role == Roles.Guest.role)
                UUID.randomUUID().toString() + ID.User.id
            else ClientPreferences.inst.userID.toString()

            val updateUser = User(
                _id = id,
                firstName = firstName.value,
                lastName = lastName.value,
                fullName = fullName.value,
                phoneNumber = phoneNumber.value.removeWhiteSpace(),
                age = age.value,
                imgUrl = imageUrl.value,
                addOrUpdateImgUrl = addOrUpdateImgUrl.value,
                eMail = eMail.value,
                //isActivite =
                role = ClientPreferences.inst.role.toString(),
                token = ClientPreferences.inst.token.toString(),
                fcmToken = ClientPreferences.inst.FCMtoken.toString(),
            )

            userFirebaseQueries.updateOrAddUser(updateUser) { status ->
                when (status) {
                    true -> {
                        successMessage.postValue(message(R.string.success))

                        ClientPreferences.inst.apply {
                            userFirstName = updateUser.firstName
                            userLastName = lastName.value
                            userFullName = fullName.value
                            userPhone = phoneNumber.value.removeWhiteSpace()
                            userAge = age.value
                            userPhotoUrl = imageUrl.value
                            userEmail = eMail.value
                            FCMtoken = updateUser.fcmToken
                            token = updateUser.token
                        }

                        if (whichComeAction.value == WhichEditProfile.LogIn)
                            ClientPreferences.inst.isBlankUserInfo = false

                        hideLoading()
                    }
                    false -> {
                        errorMessage.postValue(message(R.string.error))
                        if (whichComeAction.value == WhichEditProfile.LogIn)
                            ClientPreferences.inst.isBlankUserInfo = true
                        hideLoading()
                    }
                }
            }
        }
    }

    fun deleteUser() {
        mainScope {
            showLoading()
            var userId = ""
            var name = ""
            ClientPreferences.inst.apply {
                userId = userID.toString()
                name = this.userFirstName.toString()
            }
            userFirebaseQueries.deleteUser(userId, name) { status ->
                when (status) {
                    true -> {
                        clearClientPreferences()
                        logOut.postValue(true)
                        successMessage.postValue(message(R.string.success))
                        hideLoading()
                    }
                    false -> {
                        errorMessage.postValue(message(R.string.error))
                        hideLoading()
                    }
                }
            }
        }
    }

    fun clearClientPreferences() {
        ClientPreferences.inst.apply {
            role = null
            token = null
            FCMtoken = null
            userPhone = null
            userFirstName = null
            userLastName = null
            userFullName = null
            userEmail = null
            userPhotoUrl = null
            userID = null
            isGoogleSignIn = null
            isPhoneNumberSignIn = null
        }
    }

    fun checkRequestFields() {
        var isRequiredFieldsDone = true
        var message = ""

        if (ClientPreferences.inst.role == Roles.Guest.role) {
            if (phoneNumber.value.removeWhiteSpace().length != 13) {
                isRequiredFieldsDone = false
                message = message(R.string.error_phone_little)
            }

            if (phoneNumber.value.isNullOrEmpty()) {
                isRequiredFieldsDone = false
                message = message(R.string.phone_number_mask_optional)
            }
        }

        if (eMail.value.isNotEmpty() && !isEmailValid(eMail.value)) {
            isRequiredFieldsDone = false
            message = message(R.string.error_email)
        }

        if (isRequiredFieldsDone) {
            updateOrAddProfile()
        } else
            errorMessage.postValue(message)
    }

    //Click Listener
    fun onCstmDatePickerClick() {
        btnCstmDatePickerClick.postValue(true)
    }

    fun onUpdateClick() {
        btnUpdateAccClick.postValue(true)
    }

    fun onDeleteAccount() {
        btnDeleteAccountClick.postValue(true)
    }

    fun onImageClick() {
        imagePermission.postValue(true)
    }
}