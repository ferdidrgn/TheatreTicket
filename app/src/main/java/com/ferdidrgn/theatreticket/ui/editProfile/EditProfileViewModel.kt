package com.ferdidrgn.theatreticket.ui.editProfile

import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.base.BaseViewModel
import com.ferdidrgn.theatreticket.repository.UserFirebaseQueries
import com.ferdidrgn.theatreticket.tools.ClientPreferences
import com.ferdidrgn.theatreticket.tools.mainScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(private val userFirebaseQueries: UserFirebaseQueries) :
    BaseViewModel() {


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
        ClientPreferences.inst.token = null
        ClientPreferences.inst.FCMtoken = null
        ClientPreferences.inst.userID = null
        ClientPreferences.inst.userPhone = null
        ClientPreferences.inst.userFirstName = null
        ClientPreferences.inst.userLastName = null
        ClientPreferences.inst.userFullName = null
        ClientPreferences.inst.userEmail = null
        ClientPreferences.inst.userPhotoUrl = null
        ClientPreferences.inst.role = null
        ClientPreferences.inst.isGoogleSignIn = null
        ClientPreferences.inst.isPhoneNumberSignIn = null
    }
}