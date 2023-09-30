package com.ferdidrgn.theatreticket.ui.main.settings

import androidx.lifecycle.MutableLiveData
import com.ferdidrgn.theatreticket.base.BaseViewModel
import com.ferdidrgn.theatreticket.enums.Roles
import com.ferdidrgn.theatreticket.tools.ClientPreferences
import com.ferdidrgn.theatreticket.tools.ioScope
import kotlinx.coroutines.flow.MutableStateFlow

class SettingsViewModel : BaseViewModel() {

    val whichLayout = MutableLiveData<Boolean>()
    val userName = MutableStateFlow("")
    val role = MutableStateFlow("")


    fun selectedLayout() {
        whichLayout.postValue(false)
        when (ClientPreferences.inst.role?:"") {
            Roles.Admin.toString() -> whichLayout.postValue(true)
            Roles.User.toString() -> whichLayout.postValue(false)
            else -> whichLayout.postValue(false)
        }
    }

    fun getMyUser() {
        showLoading()
        ioScope {
            ClientPreferences.inst.userFirstName?.let { firstName ->
                ClientPreferences.inst.userLastName?.let { lastName ->
                    userName.emit(firstName + " " + lastName)
                }
            }

            ClientPreferences.inst.role?.let { role ->
                this@SettingsViewModel.role.emit(role)
            }

        }
        hideLoading()
    }
}