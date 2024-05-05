package com.ferdidrgn.theatreticket.presentation.ui.buyTicket

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.base.BaseViewModel
import com.ferdidrgn.theatreticket.commonModels.dummyData.*
import com.ferdidrgn.theatreticket.tools.enums.ID
import com.ferdidrgn.theatreticket.tools.enums.Response
import com.ferdidrgn.theatreticket.tools.enums.Roles
import com.ferdidrgn.theatreticket.data.repository.SeatFirebaseQuieries
import com.ferdidrgn.theatreticket.data.repository.SeatsFirebaseQuieries
import com.ferdidrgn.theatreticket.data.repository.SellFirebaseQueries
import com.ferdidrgn.theatreticket.domain.model.commonModels.dummyData.Seat
import com.ferdidrgn.theatreticket.domain.model.commonModels.dummyData.Seats
import com.ferdidrgn.theatreticket.domain.model.commonModels.dummyData.Sell
import com.ferdidrgn.theatreticket.domain.model.commonModels.dummyData.SellBox
import com.ferdidrgn.theatreticket.domain.model.commonModels.dummyData.Show
import com.ferdidrgn.theatreticket.domain.model.commonModels.dummyData.Stage
import com.ferdidrgn.theatreticket.tools.ClientPreferences
import com.ferdidrgn.theatreticket.tools.SELL_BOX
import com.ferdidrgn.theatreticket.tools.helpers.LiveEvent
import com.ferdidrgn.theatreticket.tools.mainScope
import com.ferdidrgn.theatreticket.tools.removeWhiteSpace
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class TicketBuyViewModel @Inject constructor(
    private val sellFirebaseQueries: SellFirebaseQueries,
    private val seatFirebaseQuieries: SeatFirebaseQuieries,
    private val seatsFirebaseQuieries: SeatsFirebaseQuieries
) : BaseViewModel() {

    val stage = MutableLiveData<Stage?>()
    val show = MutableLiveData<Show?>()

    var userId = ""
    var fullName = ""
    var firstName = ""
    var lastName = ""
    var phoneNumber = ""
    var showDetails = MutableStateFlow("")
    var chooseSeats = MutableStateFlow("")
    var chooseSeatsArray = ArrayList<String?>()
    val buyTicketPopUp = LiveEvent<Boolean?>()

    val seat = MutableLiveData<Seat?>()
    val seats = MutableLiveData<List<Seats?>?>()
    val isGetSeats = LiveEvent<Boolean?>()
    val seatColumnCount = MutableLiveData(18)

    val sellBox: ArrayList<SellBox?> = ArrayList()
    var sellAdd = Sell()

    init {
        ClientPreferences.inst.apply {
            userId = userID.toString()
            fullName = userFullName.toString()
            firstName = userFirstName.toString()
            lastName = userLastName.toString()
            phoneNumber = userPhone.toString()
        }
    }


    fun getSeat(seatId: String?) {
        mainScope {
            showLoading()
            seatFirebaseQuieries.getSeatId(seatId) { status, seatList ->
                when (status) {
                    Response.Empty -> {
                        hideLoading()
                        errorMessage.postValue(message(R.string.empty))
                    }
                    Response.ThereIs -> {
                        if (seatList == null) {
                            hideLoading()
                            errorMessage.postValue(message(R.string.error))
                        } else {
                            seat.postValue(seatList)
                            isGetSeats.postValue(true)
                            hideLoading()
                        }
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

    fun getSeats(seatId: String?) {
        mainScope {
            showLoading()
            seatsFirebaseQuieries.getSeatId(seatId) { status, seatsList ->
                when (status) {
                    Response.Empty -> {
                        hideLoading()
                        errorMessage.postValue(message(R.string.empty))
                    }
                    Response.ThereIs -> {
                        if (seatsList == null) {
                            hideLoading()
                            errorMessage.postValue(message(R.string.error))
                        } else {
                            hideLoading()
                            seats.postValue(seatsList)
                        }
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

    fun insertSeatSelection(seats: Seats?, response: (Boolean) -> Unit) {
        mainScope {
            showLoading()
            seatsFirebaseQuieries.checkIsEmptySeat(seats?._id.toString()) { status, isSelected ->
                when (status) {
                    true -> {
                        if (isSelected == true) {
                            errorMessage.postValue(message(R.string.error_seat_empty))
                            response.invoke(true)
                            hideLoading()
                        }
                        if (isSelected == false) {
                            hideLoading()
                            response.invoke(!insertSelectedSeat(seats))
                        }
                        hideLoading()
                    }
                    false -> {
                        hideLoading()
                        response.invoke(false)
                        errorMessage.postValue(message(R.string.error_server))
                    }
                }
            }
        }
    }

    private fun insertSelectedSeat(seats: Seats?): Boolean {
        var returnValue = false
        mainScope {
            showLoading()
            seatsFirebaseQuieries.updateSeats(seats) { status ->
                when (status) {
                    true -> {
                        hideLoading()
                        returnValue = true
                        chooseSeats.value = chooseSeats.value + seats?.name + ", "
                        chooseSeatsArray.add(seats?.name.toString())
                    }
                    false -> {
                        returnValue = false
                        hideLoading()
                        errorMessage.postValue(message(R.string.error_server))
                    }
                }
            }
        }
        return returnValue
    }

    fun removeSeatSelection(seats: Seats?, response: (Boolean) -> Unit) {
        mainScope {
            showLoading()
            seatsFirebaseQuieries.updateSeats(seats) { status ->
                when (status) {
                    true -> {
                        hideLoading()
                        response.invoke(true)
                        chooseSeats.value =
                            chooseSeats.value.replace(seats?.name.toString() + ", ", "").trim()
                        chooseSeatsArray.remove(seats?.name.toString())
                    }
                    false -> {
                        hideLoading()
                        response.invoke(false)
                        errorMessage.postValue(message(R.string.error_server))
                    }
                }
            }
        }
    }

    //Click Events
    fun onBtnBuyTicketClick() {
        var isRequiredFieldsDone = true
        var messge = ""

        if (ClientPreferences.inst.role == Roles.Guest.role && phoneNumber.isEmpty()) {
            isRequiredFieldsDone = false
            messge = message(R.string.guest_ticket_buy_warning)
        }

        if (chooseSeatsArray.isEmpty()) {
            isRequiredFieldsDone = false
            messge = message(R.string.choose_empty)
        }

        if (isRequiredFieldsDone) {
            buyTicketPopUp.postValue(true)
        } else
            errorMessage.postValue(messge)
    }
}