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