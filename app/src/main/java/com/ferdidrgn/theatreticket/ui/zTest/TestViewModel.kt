package com.ferdidrgn.theatreticket.ui.zTest

import androidx.lifecycle.MutableLiveData
import com.ferdidrgn.theatreticket.base.BaseViewModel
import com.ferdidrgn.theatreticket.commonModels.dummyData.Seat
import com.ferdidrgn.theatreticket.commonModels.dummyData.Show
import com.ferdidrgn.theatreticket.commonModels.dummyData.Stage
import com.ferdidrgn.theatreticket.repository.StageFirebaseQueries
import com.ferdidrgn.theatreticket.repository.UserFirebaseQueries
import com.ferdidrgn.theatreticket.tools.components.customSeat.SeatAdapterListener
import com.ferdidrgn.theatreticket.tools.mainScope
import com.ferdidrgn.theatreticket.tools.showToast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class TestViewModel @Inject constructor(
    private val userFirebaseQueries: UserFirebaseQueries
) :
    BaseViewModel(), SeatAdapterListener {

    val seat = MutableLiveData<List<Seat?>?>()
    val seatColumnCount = MutableLiveData(1)
    val nam = MutableStateFlow("")
    var stage = Stage()

    fun onBtnAddStageClick() {
    }
}