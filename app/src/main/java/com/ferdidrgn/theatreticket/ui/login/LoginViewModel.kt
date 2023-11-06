package com.ferdidrgn.theatreticket.ui.login

import com.ferdidrgn.theatreticket.base.BaseViewModel
import com.ferdidrgn.theatreticket.repository.ShowFirebaseQuieries
import com.ferdidrgn.theatreticket.tools.helpers.LiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(showFirebaseQuieries: ShowFirebaseQuieries) :
    BaseViewModel() {

    //Click Events
    val btnSignInGoogle = LiveEvent<Boolean>()
    val btnSignInPhoneNumber = LiveEvent<Boolean>()
    val btnGuest = LiveEvent<Boolean>()


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