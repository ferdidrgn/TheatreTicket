package com.ferdidrgn.theatreticket.ui.showOperations

import androidx.lifecycle.MutableLiveData
import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.base.BaseViewModel
import com.ferdidrgn.theatreticket.commonModels.dummyData.Show
import com.ferdidrgn.theatreticket.enums.Response
import com.ferdidrgn.theatreticket.repository.ShowFirebaseQuieries
import com.ferdidrgn.theatreticket.tools.helpers.LiveEvent
import com.ferdidrgn.theatreticket.tools.mainScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShowOperationsViewModel @Inject constructor(private val showFirebaseQuieries: ShowFirebaseQuieries) :
    BaseViewModel(), ShowsUpdateDeleteAdapterListener {

    val show = MutableLiveData<List<Show?>?>()
    val deleteClicked = LiveEvent<Show?>()
    val updateClicked = LiveEvent<Show?>()
    val btnAddShowClicked = LiveEvent<Boolean?>()
    val deletePopUp = LiveEvent<Boolean?>()
    val updatePopUp = LiveEvent<Boolean?>()
    var deleteIndex = 0
    var updateIndex = 0

    fun onBtnAddShow() {
        btnAddShowClicked.postValue(true)
    }

    fun getAllShow() {
        showLoading()
        mainScope {
            showFirebaseQuieries.getShow() { status, showList ->
                when (status) {
                    Response.ServerError.response -> {
                        errorMessage.postValue(message(R.string.error_server))
                        hideLoading()
                    }
                    Response.Empty.response -> {
                        errorMessage.postValue(message(R.string.error_no_any_show))
                        hideLoading()
                    }
                    Response.ThereIs.response -> {
                        showList?.let {
                            show.postValue(showList)
                        }
                        hideLoading()
                    }
                }
            }
        }
    }

    fun deleteShow() {
        showLoading()
        mainScope {
            showFirebaseQuieries.deleteShow(deleteClicked.value) { status ->
                when (status) {
                    false -> {
                        hideLoading()
                        errorMessage.postValue(message(R.string.error_server))
                    }
                    true -> {
                        show.value?.toMutableList()?.removeAt(deleteIndex)
                        show.postValue(show.value)
                        hideLoading()
                        successMessage.postValue(message(R.string.success_delete_show))
                    }
                }
            }
        }
    }

    override fun onShowsUpdateListener(position: Int, show: Show) {
        updateIndex = position
        updateClicked.postValue(show)
        updatePopUp.postValue(true)
    }

    override fun onShowsDeleteListener(position: Int, show: Show) {
        deleteIndex = position
        deleteClicked.postValue(show)
        deletePopUp.postValue(true)
    }
}