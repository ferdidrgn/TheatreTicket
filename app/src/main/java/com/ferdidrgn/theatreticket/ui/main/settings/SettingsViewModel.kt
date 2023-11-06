package com.ferdidrgn.theatreticket.ui.main.settings

import androidx.lifecycle.MutableLiveData
import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.base.BaseViewModel
import com.ferdidrgn.theatreticket.enums.Response
import com.ferdidrgn.theatreticket.enums.Roles
import com.ferdidrgn.theatreticket.enums.ToMain
import com.ferdidrgn.theatreticket.enums.WhichTermsAndPrivace
import com.ferdidrgn.theatreticket.repository.AppToolsFireBaseQueries
import com.ferdidrgn.theatreticket.repository.ShowFirebaseQuieries
import com.ferdidrgn.theatreticket.repository.UserFirebaseQueries
import com.ferdidrgn.theatreticket.tools.*
import com.ferdidrgn.theatreticket.tools.helpers.LiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val showFirebaseQuieries: ShowFirebaseQuieries,
    private val appToolsFireBaseQueries: AppToolsFireBaseQueries,
    private val userFirebaseQueries: UserFirebaseQueries
) :
    BaseViewModel() {

    val whichLayout = MutableLiveData<Boolean>()
    val userName = MutableStateFlow("")
    val role = MutableStateFlow("")

    //Click Events
    val btnSellTicketClicked = LiveEvent<Boolean>()
    val btnShowOperationsClicked = LiveEvent<Boolean>()
    val btnStageOperationsClicked = LiveEvent<Boolean>()
    val btnLanguageClicked = LiveEvent<Boolean>()
    val btnLogoutClicked = LiveEvent<Boolean>()
    val btnDeleteAccClicked = LiveEvent<Boolean>()
    val btnOnShareAppClick = LiveEvent<Boolean>()
    val btnRateAppClicked = LiveEvent<Boolean>()
    val btnContactUsClicked = LiveEvent<Boolean>()
    val btnChangeThemeClicked = LiveEvent<Boolean>()
    val btnPrivacePolicyClicked = LiveEvent<Boolean>()
    val btnTermsAndConditionsClicked = LiveEvent<Boolean>()


    fun selectedLayout() {
        whichLayout.postValue(false)
        ClientPreferences.inst.role = Roles.Admin.role //TEST - MOCKDATA
        when (ClientPreferences.inst.role) {
            Roles.Admin.role -> whichLayout.postValue(true)
            Roles.User.role -> whichLayout.postValue(false)
            Roles.GUEST.role -> whichLayout.postValue(false)
            else -> whichLayout.postValue(false)
        }
    }

    fun getMyUser() {
        showLoading()
        ioScope {
            ClientPreferences.inst.userFirstName?.let { firstName ->
                ClientPreferences.inst.userLastName?.let { lastName ->
                    userName.emit("$firstName $lastName")
                }
            }

            ClientPreferences.inst.role?.let { role ->
                this@SettingsViewModel.role.emit(role)
            }

        }
        hideLoading()
    }

    fun getContactUs(): String {
        var valueReturn = ""
        mainScope {
            showLoading()
            appToolsFireBaseQueries.getContactUsEmail { status, contactUs ->
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
        ClientPreferences.inst.token = ""
        ClientPreferences.inst.FCMtoken = ""
        ClientPreferences.inst.userID = ""
        ClientPreferences.inst.userPhone = ""
        ClientPreferences.inst.userFirstName = ""
        ClientPreferences.inst.userLastName = ""
    }

    //Click Events
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
    fun onDeleteAccClick() {
        btnDeleteAccClicked.postValue(true)
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
        btnPrivacePolicyClicked.postValue(true)
    }
    fun onTermsAndConditionsClick() {
        btnTermsAndConditionsClicked.postValue(true)
    }

}