package com.ferdidrgn.theatreticket.ui.main.settings

import androidx.lifecycle.MutableLiveData
import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.base.BaseViewModel
import com.ferdidrgn.theatreticket.enums.Response
import com.ferdidrgn.theatreticket.enums.Roles
import com.ferdidrgn.theatreticket.repository.AppToolsFireBaseQueries
import com.ferdidrgn.theatreticket.repository.ShowFirebaseQuieries
import com.ferdidrgn.theatreticket.tools.ClientPreferences
import com.ferdidrgn.theatreticket.tools.ioScope
import com.ferdidrgn.theatreticket.tools.mainScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val showFirebaseQuieries: ShowFirebaseQuieries,
    private val appToolsFireBaseQueries: AppToolsFireBaseQueries
) :
    BaseViewModel() {

    val whichLayout = MutableLiveData<Boolean>()
    val userName = MutableStateFlow("")
    val role = MutableStateFlow("")


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

    fun clearClientPreferences() {
        ClientPreferences.inst.token = ""
        ClientPreferences.inst.FCMtoken = ""
        ClientPreferences.inst.userID = ""
        ClientPreferences.inst.userPhone = ""
        ClientPreferences.inst.userFirstName = ""
        ClientPreferences.inst.userLastName = ""
    }
}