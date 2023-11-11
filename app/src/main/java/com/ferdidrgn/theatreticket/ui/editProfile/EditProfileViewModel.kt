package com.ferdidrgn.theatreticket.ui.editProfile

import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.base.BaseViewModel
import com.ferdidrgn.theatreticket.commonModels.dummyData.User
import com.ferdidrgn.theatreticket.enums.ID
import com.ferdidrgn.theatreticket.enums.Roles
import com.ferdidrgn.theatreticket.repository.UserFirebaseQueries
import com.ferdidrgn.theatreticket.tools.ClientPreferences
import com.ferdidrgn.theatreticket.tools.helpers.LiveEvent
import com.ferdidrgn.theatreticket.tools.isEmailValid
import com.ferdidrgn.theatreticket.tools.mainScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.*
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(private val userFirebaseQueries: UserFirebaseQueries) :
    BaseViewModel() {

    val fullName = MutableStateFlow("")
    val firstName = MutableStateFlow("")
    val lastName = MutableStateFlow("")
    val phoneNumber = MutableStateFlow("")
    val age = MutableStateFlow("")
    val imgPhoto = MutableStateFlow("")
    val eMail = MutableStateFlow("")

    val btnCstmDatePickerClick = LiveEvent<Boolean?>()
    val btnUpdateAccClick = LiveEvent<Boolean?>()
    val btnDeleteAccountClick = LiveEvent<Boolean?>()

    val logOut = LiveEvent<Boolean?>()


    init {
        getUser()
    }

    private fun getUser() {
        mainScope {
            showLoading()
            ClientPreferences.inst.apply {
                fullName.value = userFullName.toString()
                firstName.value = userFirstName.toString()
                lastName.value = userLastName.toString()
                phoneNumber.value = userPhone.toString()
                age.value = userAge.toString()
                imgPhoto.value = userPhotoUrl.toString()
                eMail.value = userEmail.toString()
            }
            hideLoading()
        }
    }

    fun updateOrAddProfile() {
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
                phoneNumber = phoneNumber.value,
                age = age.value,
                imgUrl = imgPhoto.value,
                eMail = eMail.value
            )

            userFirebaseQueries.updateOrAddUser(updateUser) { status ->
                when (status) {
                    true -> {
                        successMessage.postValue(message(R.string.success))

                        ClientPreferences.inst.apply {
                            userFirstName = firstName.value
                            userLastName = lastName.value
                            userFullName = fullName.value
                            userPhone = phoneNumber.value
                            userAge = age.value
                            userPhotoUrl = imgPhoto.value
                            userEmail = eMail.value
                        }
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

    fun deleteUser() {
        mainScope {
            showLoading()

            userFirebaseQueries.deleteUser { status ->
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

        if (fullName.value.length < 3)
            isRequiredFieldsDone = false

        if (firstName.value.length < 2) {
            isRequiredFieldsDone = false
            message = message(R.string.error_first_name_little)
        }

        if (lastName.value.length <= 1) {
            isRequiredFieldsDone = false
            message = message(R.string.error_last_name_little)
        }

        if (phoneNumber.value.length != 13) {
            isRequiredFieldsDone = false
            message = message(R.string.error_phone_little)
        }

        if (age.value.isEmpty()) {
            isRequiredFieldsDone = false
            message = message(R.string.error_little_things)
        }

        if (eMail.value.isEmpty() || !isEmailValid(eMail.value)) {
            isRequiredFieldsDone = false
            message = message(R.string.error_email)
        }

        if (imgPhoto.value.isEmpty()) {
            isRequiredFieldsDone = false
            message = message(R.string.error_little_things)
        }

        if (isRequiredFieldsDone) {
            updateOrAddProfile()
        } else
            errorMessage.postValue(message(R.string.error_little_things))
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
}