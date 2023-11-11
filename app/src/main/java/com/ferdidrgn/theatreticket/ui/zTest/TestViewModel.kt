package com.ferdidrgn.theatreticket.ui.zTest

import androidx.lifecycle.MutableLiveData
import com.ferdidrgn.theatreticket.base.BaseViewModel
import com.ferdidrgn.theatreticket.commonModels.dummyData.Seat
import com.ferdidrgn.theatreticket.repository.UserFirebaseQueries
import com.ferdidrgn.theatreticket.tools.components.customSeat.SeatAdapterListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class TestViewModel @Inject constructor(private val userFirebaseQueries: UserFirebaseQueries) :
    BaseViewModel(), SeatAdapterListener {

    val seat = MutableLiveData<List<Seat?>?>()
    val seatColumnCount = MutableLiveData(1)

    init {
        getSeat()
    }


    private fun getSeat() {

        val seatList = ArrayList<Seat>()

        seatList?.add(Seat("1", "1", false, "1", isSelected = false))
        seatList?.add(Seat("1", "1", false, "1", isSelected = false))
        seatList?.add(Seat("1", "1", false, "1", isSelected = false))
        seatList?.add(Seat("1", "1", false, "1", isSelected = false))
        seatList?.add(Seat("1", "1", false, "1", isSelected = false))
        seatList?.add(Seat("1", "1", false, "1", isSelected = false))
        seatList?.add(Seat("1", "1", false, "1", isSelected = false))
        seatList?.add(Seat("1", "1", false, "1", isSelected = false))
        seatList?.add(Seat("1", "1", false, "1", isSelected = false))
        seatList?.add(Seat("1", "1", false, "1", isSelected = false))
        seatList?.add(Seat("1", "1", false, "1", isSelected = false))
        seatList?.add(Seat("1", "1", false, "1", isSelected = false))
        seatList?.add(Seat("1", "1", false, "1", isSelected = false))
        seatList?.add(Seat("1", "1", false, "1", isSelected = false))
        seatList?.add(Seat("1", "1", false, "1", isSelected = false))
        seatList?.add(Seat("18", "18", false, "18", isSelected = false))
        seat.postValue(seatList)

    }

}