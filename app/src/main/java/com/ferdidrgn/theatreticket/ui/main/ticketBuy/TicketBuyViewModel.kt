package com.ferdidrgn.theatreticket.ui.main.ticketBuy

import androidx.lifecycle.MutableLiveData
import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.base.BaseViewModel
import com.ferdidrgn.theatreticket.commonModels.dummyData.Seat
import com.ferdidrgn.theatreticket.commonModels.dummyData.User
import com.ferdidrgn.theatreticket.commonModels.dummyData.Sell
import com.ferdidrgn.theatreticket.enums.ID
import com.ferdidrgn.theatreticket.enums.Response
import com.ferdidrgn.theatreticket.enums.Roles
import com.ferdidrgn.theatreticket.repository.UserFirebaseQueries
import com.ferdidrgn.theatreticket.repository.SellFirebaseQueries
import com.ferdidrgn.theatreticket.tools.*
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject
import com.ferdidrgn.theatreticket.tools.helpers.LiveEvent
import kotlinx.coroutines.flow.MutableStateFlow

@HiltViewModel
class TicketBuyViewModel @Inject constructor(
    private val sellFirebaseQueries: SellFirebaseQueries,
    private val userFirebaseQueries: UserFirebaseQueries
) :
    BaseViewModel() {

    var firstName = MutableStateFlow("")
    var lastName = MutableStateFlow("")
    var phoneNumber = MutableStateFlow("")
    var age = MutableStateFlow("")
    val buyTicketPopUp = LiveEvent<Boolean?>()
    val btnCstmDatePickerClick = LiveEvent<Boolean?>()

    var userAdd = User()
    var sellAdd = Sell()

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

    private fun checkTicket(user: User?) {

        showLoading()
        mainScope {
            sellFirebaseQueries.checkBuyTicketCustomerId(user?._id.toString()) { status ->
                when (status) {
                    Response.Empty -> {
                        hideLoading()
                        fillDatas(user, false)
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

    private fun fillDatas(user: User?, isNewCustom: Boolean) {

        showLoading()

        mainScope {
            userAdd = User(
                _id = user?._id.toString(),
                token = ClientPreferences.inst.token.toString(),
                fcmToken = ClientPreferences.inst.FCMtoken.toString(),
                firstName = user?.firstName.toString(),
                lastName = user?.lastName.toString(),
                phoneNumber = user?.phoneNumber?.removeWhiteSpace(),
                age = age.value
            )

            val id = UUID.randomUUID().toString()
            sellAdd = Sell(
                _id = id + ID.Sell.id,
                customerId = user?._id.toString(),
                showId = "MockData",
                customerFullName = user?.firstName.toString() + " " + user?.lastName.toString(),
                customerPhone = user?.phoneNumber?.removeWhiteSpace(),
                showName = "MockData",
                showDate = "MockData",
                showTime = "MockData",
                showPrice = "MockData",
                showSeat = "MockData",
                stageName = "MockData",
                stageLocation = "MockData"
            )
        }

        hideLoading()
        if (isNewCustom) addUser() else buyTicket()
    }

    private fun addUser() {
        showLoading()
        mainScope {
            userFirebaseQueries.addUser(userAdd) { status ->
                if (status) {
                    hideLoading()
                    buyTicket()
                } else {
                    hideLoading()
                    errorMessage.postValue(message(R.string.error_server))
                }
            }
        }
    }

    private fun buyTicket() {
        showLoading()
        mainScope {
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

    fun checkRequestFields() {
        var isRequiredFieldsDone = true
        var message = ""
        if (firstName.value.length < 2) {
            isRequiredFieldsDone = false
            message = message(R.string.error_first_name_little)
        }

        if (lastName.value.length <= 1) {
            isRequiredFieldsDone = false
            message = message(R.string.error_last_name_little)
        }

        if (phoneNumber.value.removeWhiteSpace().length != 13) {
            isRequiredFieldsDone = false
            message = message(R.string.error_phone_little)
        }

        if (isRequiredFieldsDone) {
        } else
            errorMessage.postValue(message)
    }

    //Click Listener
    fun onCstmDatePickerClick() {
        btnCstmDatePickerClick.postValue(true)
    }
}