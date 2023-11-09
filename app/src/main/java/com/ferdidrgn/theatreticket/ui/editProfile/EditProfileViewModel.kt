package com.ferdidrgn.theatreticket.ui.editProfile

import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.base.BaseViewModel
import com.ferdidrgn.theatreticket.commonModels.dummyData.User
import com.ferdidrgn.theatreticket.repository.UserFirebaseQueries
import com.ferdidrgn.theatreticket.tools.ClientPreferences
import com.ferdidrgn.theatreticket.tools.helpers.LiveEvent
import com.ferdidrgn.theatreticket.tools.mainScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
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

    fun updateProfile() {
        mainScope {
            showLoading()

            val updateUser = User(
                ClientPreferences.inst.userID,
                firstName.value,
                lastName.value,
                fullName.value,
                phoneNumber.value,
                age.value,
                imgPhoto.value,
                eMail.value
            )

            userFirebaseQueries.updateUser(updateUser) { status ->
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

    //Click Listener
    fun onCstmDatePickerClick() {
        btnCstmDatePickerClick.postValue(true)
    }
}