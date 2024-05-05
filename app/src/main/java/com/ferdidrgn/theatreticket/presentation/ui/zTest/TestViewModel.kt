package com.ferdidrgn.theatreticket.presentation.ui.zTest

import androidx.lifecycle.MutableLiveData
import com.ferdidrgn.theatreticket.base.BaseViewModel
import com.ferdidrgn.theatreticket.domain.model.commonModels.dummyData.Seat
import com.ferdidrgn.theatreticket.domain.model.commonModels.dummyData.Stage
import com.ferdidrgn.theatreticket.data.repository.UserFirebaseQueries
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class TestViewModel @Inject constructor(
    private val userFirebaseQueries: UserFirebaseQueries
) :
    BaseViewModel() {

    val seat = MutableLiveData<List<Seat?>?>()
    val seatColumnCount = MutableLiveData(1)
    val nam = MutableStateFlow("")
    var stage = Stage()

    fun onBtnAddStageClick() {
    }
}