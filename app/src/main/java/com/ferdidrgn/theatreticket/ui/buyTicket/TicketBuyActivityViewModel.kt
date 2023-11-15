package com.ferdidrgn.theatreticket.ui.buyTicket

import androidx.lifecycle.MutableLiveData
import com.ferdidrgn.theatreticket.base.BaseViewModel
import com.ferdidrgn.theatreticket.commonModels.dummyData.*
import com.ferdidrgn.theatreticket.repository.SellFirebaseQueries
import com.ferdidrgn.theatreticket.repository.ShowFirebaseQuieries
import com.ferdidrgn.theatreticket.repository.StageFirebaseQueries
import com.ferdidrgn.theatreticket.tools.helpers.LiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class TicketBuyActivityViewModel @Inject constructor(
    private val showFirebaseQuieries: ShowFirebaseQuieries,
    private val stageFirebaseQueries: StageFirebaseQueries,
    private val sellFirebaseQueries: SellFirebaseQueries
) : BaseViewModel() {

    val stage = MutableLiveData<Stage?>()
    val show = MutableLiveData<Show?>()

    var firstName = MutableStateFlow("")
    var lastName = MutableStateFlow("")
    var phoneNumber = MutableStateFlow("")
    var age = MutableStateFlow("")
    val buyTicketPopUp = LiveEvent<Boolean?>()
    val btnCstmDatePickerClick = LiveEvent<Boolean?>()

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

    fun onBtnBuyTicketClick() {
        buyTicketPopUp.postValue(true)
    }

    //Click Listener
    fun onCstmDatePickerClick() {
        btnCstmDatePickerClick.postValue(true)
    }
}