package com.ferdidrgn.theatreticket.presentation.main.settings

import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.util.base.BaseViewModel
import com.ferdidrgn.theatreticket.util.Response
import com.ferdidrgn.theatreticket.util.Roles
import com.ferdidrgn.theatreticket.data.repository.UserFirebaseQueries
import com.ferdidrgn.theatreticket.domain.useCase.contactUsEmail.GetContactUsEmailUseCase
import com.ferdidrgn.theatreticket.util.ClientPreferences
import com.ferdidrgn.theatreticket.util.helpers.LiveEvent
import com.ferdidrgn.theatreticket.util.mainScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getContactUsEmailUseCase: GetContactUsEmailUseCase,
    private val userFirebaseQueries: UserFirebaseQueries
) :
    BaseViewModel() {

    val userFullName = MutableStateFlow("")
    val roleAdminLayout = LiveEvent<Boolean>()
    val roleGuestLayout = LiveEvent<Boolean>()


    //Click Events
    val btnSellTicketClicked = LiveEvent<Boolean>()
    val btnShowOperationsClicked = LiveEvent<Boolean>()
    val btnStageOperationsClicked = LiveEvent<Boolean>()
    val btnLanguageClicked = LiveEvent<Boolean>()
    val btnLogoutClicked = LiveEvent<Boolean>()
    val btnLogInClicked = LiveEvent<Boolean>()
    val btnNotificationPermission = LiveEvent<Boolean>()
    val btnOnShareAppClick = LiveEvent<Boolean>()
    val btnRateAppClicked = LiveEvent<Boolean>()
    val btnContactUsClicked = LiveEvent<Boolean>()
    val btnChangeThemeClicked = LiveEvent<Boolean>()
    val btnPrivacyPolicyClicked = LiveEvent<Boolean>()
    val btnTermsAndConditionsClicked = LiveEvent<Boolean>()
    val btnEditProfileClicked = LiveEvent<Boolean>()
    val btnBuyCoffeeGooglePlayClick = LiveEvent<Boolean>()


    fun selectedLayout() {
        when (ClientPreferences.inst.role) {
            Roles.Admin.role -> {
                roleAdminLayout.postValue(true)
                roleGuestLayout.postValue(false)
            }

            Roles.Guest.role -> {
                roleAdminLayout.postValue(false)
                roleGuestLayout.postValue(true)
            }

            Roles.User.role -> {
                roleAdminLayout.postValue(false)
                roleGuestLayout.postValue(false)
            }
        }

    }

    fun getMyUser() {
        showLoading()
        mainScope {
            if (ClientPreferences.inst.isGoogleSignIn == true) {
                ClientPreferences.inst.userFullName?.let { fullName ->
                    userFullName.emit(fullName)
                }
            } else {
                ClientPreferences.inst.userFirstName?.let { firstName ->
                    ClientPreferences.inst.userLastName?.let { lastName ->
                        userFullName.emit("$firstName $lastName")
                    }
                }
            }
            hideLoading()
        }
    }

    fun getContactUs(): String {
        var valueReturn = ""
        mainScope {
            showLoading()
            getContactUsEmailUseCase { status, contactUs ->
                when (status) {
                    Response.ThereIs -> {
                        contactUs?.let { data ->
                            valueReturn = data
                        }
                        hideLoading()
                    }

                    Response.Empty -> {
                        errorMessage.postValue(message(R.string.error))
                        hideLoading()
                    }

                    Response.ServerError -> {
                        errorMessage.postValue(message(R.string.error_server))
                        hideLoading()
                    }

                    else -> {
                        errorMessage.postValue(message(R.string.error))
                        hideLoading()
                    }
                }
            }
        }
        return valueReturn
    }

    fun checkRole() {
        mainScope {
            showLoading()
            userFirebaseQueries.checkRole(ClientPreferences.inst.userID.toString()) { status, roleNull ->
                when (status) {
                    Response.ThereIs -> {
                        roleNull?.let { role ->
                            ClientPreferences.inst.role = role
                            selectedLayout()
                        }
                        hideLoading()
                    }

                    Response.Empty -> {
                        errorMessage.postValue(message(R.string.error))
                        hideLoading()
                    }

                    Response.ServerError -> {
                        errorMessage.postValue(message(R.string.error_server))
                        hideLoading()
                    }

                    else -> {
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

    //Click Events
    fun onEditProfileClick() {
        btnEditProfileClicked.postValue(true)
    }

    fun onSellTicketClick() {
        btnSellTicketClicked.postValue(true)
    }

    fun onShowOperationsClick() {
        btnShowOperationsClicked.postValue(true)
    }

    fun onStageOperationsClick() {
        btnStageOperationsClicked.postValue(true)
    }

    fun onLanguageClick() {
        btnLanguageClicked.postValue(true)
    }

    fun onLogoutClick() {
        btnLogoutClicked.postValue(true)
    }

    fun onLogInClick() {
        btnLogInClicked.postValue(true)
    }

    fun onNotificationPermissionClick() {
        btnNotificationPermission.postValue(true)
    }

    fun onShareAppClick() {
        btnOnShareAppClick.postValue(true)
    }

    fun onRateAppClick() {
        btnRateAppClicked.postValue(true)
    }

    fun onContactUsClick() {
        btnContactUsClicked.postValue(true)
    }

    fun onChangeThemeClick() {
        btnChangeThemeClicked.postValue(true)
    }

    fun onPrivacePolicyClick() {
        btnPrivacyPolicyClicked.postValue(true)
    }

    fun onTermsAndConditionsClick() {
        btnTermsAndConditionsClicked.postValue(true)
    }

    fun onBuyCoffeeGooglePlayClick() {
        btnBuyCoffeeGooglePlayClick.postValue(true)
    }

}