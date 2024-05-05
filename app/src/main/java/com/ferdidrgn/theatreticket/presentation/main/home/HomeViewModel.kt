package com.ferdidrgn.theatreticket.presentation.main.home

import androidx.lifecycle.MutableLiveData
import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.util.base.BaseViewModel
import com.ferdidrgn.theatreticket.domain.model.commonModels.dummyData.Show
import com.ferdidrgn.theatreticket.util.Response
import com.ferdidrgn.theatreticket.data.repository.ShowFirebaseQuieries
import com.ferdidrgn.theatreticket.util.helpers.LiveEvent
import com.ferdidrgn.theatreticket.util.mainScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val showFirebaseQuieries: ShowFirebaseQuieries) :
    BaseViewModel(), ShowDetailsAdapterListener, ShowSliderDetailsAdapterListener {

    val show = MutableLiveData<List<Show?>?>()
    val goShowDetails = LiveEvent<Show?>()

    fun getAllShow() {
        showLoading()
        mainScope {
            showFirebaseQuieries.getShow() { status, showList ->
                when (status) {
                    Response.ServerError -> {
                        errorMessage.postValue(message(R.string.error_server))
                        hideLoading()
                    }
                    Response.Empty -> {
                        errorMessage.postValue(message(R.string.error_no_any_show))
                        hideLoading()
                    }
                    Response.ThereIs -> {
                        showList?.let {
                            show.postValue(showList)
                        }
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

    override fun onShowDetailsAdapterListener(show: Show?) {
        goShowDetails.postValue(show)
    }

    override fun onShowSliderDetailsAdapterListener(show: Show) {
        goShowDetails.postValue(show)
    }

}