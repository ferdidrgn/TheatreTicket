package com.ferdidrgn.theatreticket.ui.stageOperations

import androidx.lifecycle.MutableLiveData
import com.ferdidrgn.theatreticket.base.BaseViewModel
import com.ferdidrgn.theatreticket.commonModels.dummyData.Stage
import com.ferdidrgn.theatreticket.repository.UserFirebaseQueries
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class StageOperationsViewModel @Inject constructor(private val userFirebaseQueries: UserFirebaseQueries) :
    BaseViewModel() {

    val stage = MutableLiveData<List<Stage?>?>()
    var name = MutableStateFlow("")
    var description = MutableStateFlow("")
    var communication = MutableStateFlow("")
    var capacity = MutableStateFlow("")
    var location = MutableStateFlow("")
    val locationLat = MutableStateFlow<Double>(0.0)
    val locationLng = MutableStateFlow<Double>(0.0)
    val seatId = MutableStateFlow("")
    var seatColumnCount = MutableStateFlow<Int>(0)
    var seatRowCount = MutableStateFlow<Int>(0)
}