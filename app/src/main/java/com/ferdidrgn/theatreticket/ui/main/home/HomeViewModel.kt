package com.ferdidrgn.theatreticket.ui.main.home

import androidx.lifecycle.MutableLiveData
import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.base.BaseViewModel
import com.ferdidrgn.theatreticket.commonModels.dummyData.Show
import com.ferdidrgn.theatreticket.enums.Response
import com.ferdidrgn.theatreticket.repository.ShowFirebaseQuieries
import com.ferdidrgn.theatreticket.tools.NavHandler
import com.ferdidrgn.theatreticket.tools.mainScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val showFirebaseQuieries: ShowFirebaseQuieries) :
    BaseViewModel(), ShowDetailsAdapterListener, ShowSliderDetailsAdapterListener {

    val show = MutableLiveData<List<Show?>?>()
    val goShowDetails = MutableLiveData<ArrayList<Show?>?>()

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

    override fun onShowDetailsAdapterListener(show: Show?) {
        val showList = ArrayList<Show?>()
        showList.add(show)
        goShowDetails.postValue(showList)
    }

    override fun onShowSliderDetailsAdapterListener(show: Show) {
        val showList = ArrayList<Show?>()
        showList.add(show)
        goShowDetails.postValue(showList)
    }

}