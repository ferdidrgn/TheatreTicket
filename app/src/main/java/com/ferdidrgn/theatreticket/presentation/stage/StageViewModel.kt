package com.ferdidrgn.theatreticket.presentation.stage

import androidx.lifecycle.MutableLiveData
import com.ferdidrgn.theatreticket.util.base.BaseViewModel
import com.ferdidrgn.theatreticket.domain.model.commonModels.dummyData.Stage
import com.ferdidrgn.theatreticket.data.repository.StageFirebaseQueries
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject


@HiltViewModel
class StageViewModel @Inject constructor(private val stageFirebaseQueries: StageFirebaseQueries) :
    BaseViewModel() {

    val stage = MutableLiveData<Stage?>()
    val stageName = MutableStateFlow("")

}