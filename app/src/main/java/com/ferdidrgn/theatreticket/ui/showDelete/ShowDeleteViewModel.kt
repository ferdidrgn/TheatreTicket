package com.ferdidrgn.theatreticket.ui.showDelete

import androidx.lifecycle.MutableLiveData
import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.base.BaseViewModel
import com.ferdidrgn.theatreticket.commonModels.dummyData.Show
import com.ferdidrgn.theatreticket.enums.Response
import com.ferdidrgn.theatreticket.tools.mainScope
import com.ferdidrgn.theatreticket.ui.main.home.ShowDetailsAdapterListener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShowDeleteViewModel @Inject constructor(private val forFirebaseQueries: ForFirebaseQueries) :
    BaseViewModel(), ShowDetailsAdapterListener {

    val show = MutableLiveData<List<Show?>?>()

    fun getAllShow() {
        showLoading()
        mainScope {
            forFirebaseQueries.getShow() { status, showList ->
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
}