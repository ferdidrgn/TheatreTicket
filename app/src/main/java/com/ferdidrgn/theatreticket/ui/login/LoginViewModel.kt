package com.ferdidrgn.theatreticket.ui.login

import androidx.lifecycle.MutableLiveData
import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.base.BaseViewModel
import com.ferdidrgn.theatreticket.commonModels.dummyData.User
import com.ferdidrgn.theatreticket.enums.Response
import com.ferdidrgn.theatreticket.repository.UserFirebaseQueries
import com.ferdidrgn.theatreticket.tools.ClientPreferences
import com.ferdidrgn.theatreticket.tools.helpers.LiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val userFirebaseQueries: UserFirebaseQueries) :
    BaseViewModel() {

    //Click Events
    val btnSignInGoogle = LiveEvent<Boolean>()
    val btnSignInPhoneNumber = LiveEvent<Boolean>()
    val btnGuest = LiveEvent<Boolean>()
    var userAdd = User()
    var isFirebaseUserControl = MutableLiveData<Boolean>()


    fun addUser() {
        userAdd = User(
            _id = ClientPreferences.inst.userID,
            firstName = ClientPreferences.inst.userFirstName,
            lastName = ClientPreferences.inst.userLastName,
            fullName = ClientPreferences.inst.userFullName,
            phoneNumber = ClientPreferences.inst.userPhone,
            role = ClientPreferences.inst.role,
            age = ClientPreferences.inst.userAge,
            eMail = ClientPreferences.inst.userEmail,
        )
        userFirebaseQueries.checkUserId(userAdd) { status, userInfoList ->
            when (status) {
                Response.Empty -> {
                    userFirebaseQueries.addUser(userAdd) { status ->
                        if (status) {
                            hideLoading()
                        } else {
                            hideLoading()
                            errorMessage.postValue(message(R.string.error_add_user))
                        }
                    }
                }
                Response.ThereIs -> {
                    ClientPreferences.inst.apply {
                        role = userInfoList?.role.toString()
                        userID = userInfoList?._id
                        userFullName = userInfoList?.fullName
                        userEmail = userInfoList?.eMail
                        userPhone = userInfoList?.phoneNumber
                        userPhotoUrl = userInfoList?.imgUrl.toString()
                    }
                    hideLoading()
                    isFirebaseUserControl.postValue(true)
                }
                Response.NotEqual -> {
                    hideLoading()
                    errorMessage.postValue(message(R.string.error_not_equal))
                }
                Response.ServerError -> {
                    hideLoading()
                    errorMessage.postValue(message(R.string.error_server))
                    isFirebaseUserControl.postValue(false)
                }
            }

        }
    }

    //Click Events
    fun onGoogleSignInClicked() {
        btnSignInGoogle.postValue(true)
    }

    fun onPhoneNumberSignInClicked() {
        btnSignInPhoneNumber.postValue(true)
    }

    fun onGuestClicked() {
        btnGuest.postValue(true)
    }
}