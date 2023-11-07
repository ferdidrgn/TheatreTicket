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
}