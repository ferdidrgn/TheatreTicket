package com.ferdidrgn.theatreticket.presentation.login

import androidx.lifecycle.MutableLiveData
import com.ferdidrgn.theatreticket.util.base.BaseViewModel
import com.ferdidrgn.theatreticket.data.repository.UserFirebaseQueries
import com.ferdidrgn.theatreticket.util.helpers.LiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val userFirebaseQueries: UserFirebaseQueries) :
    BaseViewModel() {

    //Click Events
    val btnSignInGoogle = LiveEvent<Boolean>()
    val btnSignInPhoneNumber = LiveEvent<Boolean>()
    val btnGuest = LiveEvent<Boolean>()
    var isFirebaseUserControl = MutableLiveData<Boolean>()

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