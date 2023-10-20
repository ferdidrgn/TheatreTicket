package com.ferdidrgn.theatreticket.ui.showAdd

import com.ferdidrgn.theatreticket.R
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

    val showAdd = Show()

    fun onAddImageClick() {
        //addImage.postValue(true)
    }

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
                        successMessage.postValue(message(R.string.success_add_show))
                    }
                    false -> {
                        hideLoading()
                        errorMessage.postValue(message(R.string.error_server))
                    }
                }
            }
        }
    }
}