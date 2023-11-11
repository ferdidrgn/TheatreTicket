package com.ferdidrgn.theatreticket.ui.showDetails

import androidx.lifecycle.MutableLiveData
import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.base.BaseViewModel
import com.ferdidrgn.theatreticket.commonModels.dummyData.Show
import com.ferdidrgn.theatreticket.commonModels.dummyData.Stage
import com.ferdidrgn.theatreticket.enums.Response
import com.ferdidrgn.theatreticket.repository.ShowFirebaseQuieries
import com.ferdidrgn.theatreticket.repository.StageFirebaseQueries
import com.ferdidrgn.theatreticket.tools.helpers.LiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShowDetailsViewModel @Inject constructor(
    private val stageFirebaseQueries: StageFirebaseQueries
) : BaseViewModel() {

    val show = MutableLiveData<Show?>()
    val stage = MutableLiveData<Stage?>()

    val btnStageOnClick = LiveEvent<Boolean?>()
    init {
        getStageId()
    }

    fun onBtnStageClick() {
        btnStageOnClick.postValue(true)
    }

    private fun getStageId() {
        stageFirebaseQueries.getStageId(show.value?.stageId) { response, stages ->
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