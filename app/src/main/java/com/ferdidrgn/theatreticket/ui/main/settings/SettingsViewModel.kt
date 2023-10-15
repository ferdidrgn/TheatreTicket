package com.ferdidrgn.theatreticket.ui.main.settings

import androidx.lifecycle.MutableLiveData
import com.ferdidrgn.theatreticket.base.BaseViewModel
import com.ferdidrgn.theatreticket.enums.Roles
import com.ferdidrgn.theatreticket.repository.ForFirebaseQueries
import com.ferdidrgn.theatreticket.tools.ClientPreferences
import com.ferdidrgn.theatreticket.tools.ioScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val forFirebaseQueries: ForFirebaseQueries) :
    BaseViewModel() {

    val whichLayout = MutableLiveData<Boolean>()
    val userName = MutableStateFlow("")
    val role = MutableStateFlow("")


    fun selectedLayout() {
        whichLayout.postValue(false)
        ClientPreferences.inst.role = Roles.Admin.role //TEST - MOCKDATA
        when (ClientPreferences.inst.role ?: "") {
            Roles.Admin.role -> whichLayout.postValue(true)
            Roles.User.role -> whichLayout.postValue(false)
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