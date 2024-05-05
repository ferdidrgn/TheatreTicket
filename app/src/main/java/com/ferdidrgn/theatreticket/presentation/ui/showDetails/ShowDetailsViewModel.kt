package com.ferdidrgn.theatreticket.presentation.ui.showDetails

import androidx.lifecycle.MutableLiveData
import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.base.BaseViewModel
import com.ferdidrgn.theatreticket.domain.model.commonModels.dummyData.Show
import com.ferdidrgn.theatreticket.domain.model.commonModels.dummyData.Stage
import com.ferdidrgn.theatreticket.tools.enums.Response
import com.ferdidrgn.theatreticket.data.repository.ShowFirebaseQuieries
import com.ferdidrgn.theatreticket.data.repository.StageFirebaseQueries
import com.ferdidrgn.theatreticket.tools.helpers.LiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShowDetailsViewModel @Inject constructor(
    private val stageFirebaseQueries: StageFirebaseQueries,
    private val showFirebaseQueries: ShowFirebaseQuieries
) : BaseViewModel() {

    val show = MutableLiveData<Show?>()
    val stage = MutableLiveData<Stage?>()

    val btnStageOnClick = LiveEvent<Boolean?>()
    val btnSeatOnClick = LiveEvent<Boolean?>()

    fun onBtnStageClick() {
        btnStageOnClick.postValue(true)
    }

    fun onBtnSeatClick() {
        btnSeatOnClick.postValue(true)
    }

    fun getShowId(showId: String) {
        showFirebaseQueries.getShowId(showId) { response, shows ->
            when (response) {
                Response.Empty -> {
                    errorMessage.postValue(message(R.string.error))
                }
                Response.ServerError -> {
                    errorMessage.postValue(message(R.string.error_server))
                }
                Response.ThereIs -> {
                    show.postValue(shows)
                }
                else -> {
                    errorMessage.postValue(message(R.string.error))
                }
            }
        }
    }

    fun getStageId(stageId: ArrayList<Any>?) {
        stageFirebaseQueries.getStageId(stageId) { response, stages ->
            when (response) {
                Response.Empty -> {
                    errorMessage.postValue(message(R.string.error))
                }
                Response.ServerError -> {
                    errorMessage.postValue(message(R.string.error_server))
                }
                Response.ThereIs -> {
                    stage.postValue(stages?.first())
                }
                else -> {
                    errorMessage.postValue(message(R.string.error))
                }
            }

        }
    }
}