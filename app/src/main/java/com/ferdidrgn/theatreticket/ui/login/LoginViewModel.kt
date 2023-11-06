package com.ferdidrgn.theatreticket.ui.login

import com.ferdidrgn.theatreticket.base.BaseViewModel
import com.ferdidrgn.theatreticket.repository.ShowFirebaseQuieries
import com.ferdidrgn.theatreticket.tools.helpers.LiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(showFirebaseQuieries: ShowFirebaseQuieries) :
    BaseViewModel() {

    //click events
    val btnSignInGoogle = LiveEvent<Boolean>()
    val btnSignInPhoneNumber = LiveEvent<Boolean>()

    //click events
    fun onGoogleSignInClicked() {
        btnSignInGoogle.postValue(true)
    }

    fun onPhoneNumberSignInClicked() {
        btnSignInPhoneNumber.postValue(true)
    }
}