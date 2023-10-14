package com.ferdidrgn.theatreticket.ui.showAddActivity

import com.ferdidrgn.theatreticket.base.BaseViewModel
import com.ferdidrgn.theatreticket.commonModels.dummyData.Show
import com.ferdidrgn.theatreticket.repository.ForFirebaseQueries
import com.ferdidrgn.theatreticket.tools.helpers.LiveEvent
import com.ferdidrgn.theatreticket.tools.mainScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class ShowAddViewModel @Inject constructor(private val forFirebaseQueries: ForFirebaseQueries) :
    BaseViewModel() {

    val name = MutableStateFlow("")
    val desc = MutableStateFlow("")
    val imageUrl = MutableStateFlow("")
    val date = MutableStateFlow("")
    val price = MutableStateFlow("")
    val ageLimit = MutableStateFlow("")
    val stage = MutableStateFlow("")
    val players = MutableStateFlow("")
    val addShowPopUp = LiveEvent<Boolean?>()
    val errorMessage = LiveEvent<String?>()
    val successMessage = LiveEvent<String?>()

    val showAdd = Show()

    fun onBtnAddShowClick() {
        addShowPopUp.postValue(true)
    }

    fun addShow() {
        showLoading()
        mainScope {
            forFirebaseQueries.addShow(showAdd) { status ->
                when (status) {
                    true -> {
                        hideLoading()
                    }
                    false -> {
                        hideLoading()
                    }
                }
            }
        }
    }
}