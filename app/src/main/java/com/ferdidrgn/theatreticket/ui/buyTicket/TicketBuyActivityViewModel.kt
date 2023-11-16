package com.ferdidrgn.theatreticket.ui.buyTicket

import androidx.lifecycle.MutableLiveData
import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.base.BaseViewModel
import com.ferdidrgn.theatreticket.commonModels.dummyData.*
import com.ferdidrgn.theatreticket.enums.ID
import com.ferdidrgn.theatreticket.enums.Response
import com.ferdidrgn.theatreticket.enums.Roles
import com.ferdidrgn.theatreticket.repository.SellFirebaseQueries
import com.ferdidrgn.theatreticket.tools.ClientPreferences
import com.ferdidrgn.theatreticket.tools.helpers.LiveEvent
import com.ferdidrgn.theatreticket.tools.mainScope
import com.ferdidrgn.theatreticket.tools.removeWhiteSpace
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class TicketBuyActivityViewModel @Inject constructor(
    private val sellFirebaseQueries: SellFirebaseQueries,
) : BaseViewModel() {

    val stage = MutableLiveData<Stage?>()
    val show = MutableLiveData<Show?>()

    var userId = ""
    var fullName = ""
    var firstName = ""
    var lastName = ""
    var phoneNumber = ""
    var showDetails = ""
    var chooseSeats = MutableStateFlow("")
    val buyTicketPopUp = LiveEvent<Boolean?>()

    val seat = MutableLiveData<List<Seat?>?>()
    val seatColumnCount = MutableLiveData(18)

    var sellAdd = Sell()

    init {
        ClientPreferences.inst.apply {
            userId = userID.toString()
            fullName = userFullName.toString()
            firstName = userFirstName.toString()
            lastName = userLastName.toString()
            phoneNumber = userPhone.toString()
        }
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

    fun checkTicket() {
        showLoading()
        mainScope {
            sellFirebaseQueries.checkBuyTicketCustomerId(userId) { status ->
                when (status) {
                    Response.Empty -> {
                        hideLoading()
                        buyTicket()
                    }
                    Response.ThereIs -> {
                        hideLoading()
                        errorMessage.postValue(message(R.string.error_have_ticket))
                    }
                    Response.ServerError -> {
                        hideLoading()
                        errorMessage.postValue(message(R.string.error_server))
                    }
                    else -> {
                        hideLoading()
                        errorMessage.postValue(message(R.string.error))
                    }
                }
            }
        }
    }

    private fun buyTicket() {
        mainScope {
            showLoading()

            val id = UUID.randomUUID().toString()
            sellAdd = Sell(
                _id = id + ID.Sell.id,
                customerId = userId,
                showId = show?.value?._id,
                customerFullName = if (ClientPreferences.inst.isGoogleSignIn == true) fullName else firstName + " " + lastName,
                customerPhone = phoneNumber?.removeWhiteSpace(),
                showName = show.value?.name,
                showDate = show.value?.date,
                showTime = show.value?.time,
                showPrice = show.value?.price,
                showSeat = chooseSeats.value,
                stageName = stage.value?.name,
                stageLocation = stage.value?.address,
            )
            sellFirebaseQueries.addSales(sellAdd) { status ->
                if (status) {
                    hideLoading()
                    successMessage.postValue(message(R.string.success))
                } else {
                    hideLoading()
                    errorMessage.postValue(message(R.string.error_server))
                }
            }
        }
    }

    fun onBtnBuyTicketClick() {
        var isRequiredFieldsDone = true
        var messge = ""

        if (ClientPreferences.inst.role == Roles.Guest.role && phoneNumber.isEmpty()) {
            isRequiredFieldsDone = false
            messge = message(R.string.guest_ticket_buy_warning)
        }

        if (chooseSeats.value.isEmpty()) {
            isRequiredFieldsDone = false
            messge = message(R.string.choose_empty)
        }

        if (isRequiredFieldsDone) {
            buyTicketPopUp.postValue(true)
        } else
            errorMessage.postValue(messge)
    }
}