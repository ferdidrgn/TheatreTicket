package com.ferdidrgn.theatreticket.ui.main.ticketBuy

import android.content.res.Resources
import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.base.BaseViewModel
import com.ferdidrgn.theatreticket.commonModels.dummyData.Customer
import com.ferdidrgn.theatreticket.commonModels.dummyData.Sell
import com.ferdidrgn.theatreticket.enums.ID
import com.ferdidrgn.theatreticket.enums.Response
import com.ferdidrgn.theatreticket.forFirebaseQueries.ForFirebaseQueries
import com.ferdidrgn.theatreticket.tools.ClientPreferences
import com.ferdidrgn.theatreticket.tools.ioScope
import com.ferdidrgn.theatreticket.tools.showToast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.UUID
import javax.inject.Inject
import com.ferdidrgn.theatreticket.tools.helpers.LiveEvent
import com.ferdidrgn.theatreticket.tools.mainScope
import kotlinx.coroutines.flow.collectLatest

@HiltViewModel
class TicketBuyViewModel @Inject constructor(private val forFireBaseQueries: ForFirebaseQueries) :
    BaseViewModel() {

    var firstName = MutableStateFlow("")
    var lastName = MutableStateFlow("")
    var phoneNumber = MutableStateFlow("")
    var age = MutableStateFlow("")
    val buyTicketPopUp = LiveEvent<Boolean?>()
    val errorMessage = LiveEvent<String?>()
    var uuid = MutableStateFlow("")

    var customerAdd = Customer()
    var sellAdd = Sell()

    fun onBtnBuyTicketClick() {
        var isRequiredFieldsDone = true

        mainScope {
            /*firstName.collectLatest {
                if (firstName.value.length < 2) {
                    isRequiredFieldsDone = false
                    errorMessage.value = message(R.string.error_first_name_little)
                }
            }

            lastName.collectLatest {
                if (lastName.value.length < 1) {
                    isRequiredFieldsDone = false
                    errorMessage.value = message(R.string.error_last_name_little)
                }
            }*/
            phoneNumber.collectLatest {
                if (phoneNumber.value.length < 10) {
                    isRequiredFieldsDone = false
                    errorMessage.value = message(R.string.error_phone_little)
                }
            }
            if (isRequiredFieldsDone) {
                buyTicketPopUp.postValue(true)
            }
        }
    }

    fun checkPhoneNumber() {
        mainScope {
            showLoading()
            val (response, customerId) = forFireBaseQueries.checkPhoneNumber(phoneNumber.value)
            uuid.value = customerId
            when (response) {
                Response.ServerError.response -> {
                    //errorMessage.value = message(R.string.error_server)
                }

                Response.ThereIs.response -> {
                    val checkBuyTicket = forFireBaseQueries.checkBuyTicket(uuid.value)
                    when (checkBuyTicket) {
                        Response.ServerError.response -> {
                            //errorMessage.value = messageR.string.error_server)
                        }

                        Response.ThereIs.response -> {
                            //message(R.string.error_have_ticket)
                        }

                        Response.Empty.response -> {
                            //checkPhoneResume()
                        }

                    }
                }

                Response.Empty.response -> {
                    //Universal Unique ID
                    uuid.value = UUID.randomUUID().toString()
                    customerAdd = Customer(
                        _id = uuid.value + ID.Customer.id,
                        firstName = firstName.value,
                        lastName = lastName.value,
                        phoneNumber = phoneNumber.value
                        //age = userAge.value
                    )
                    //checkPhoneResume()
                }

                else -> showToast("else çalıştı")//errorMessage.value = message(R.string.error_server)
            }
        }
        hideLoading()
    }


    private fun checkPhoneResume() {

        ioScope {
            showLoading()

            sellAdd = Sell(
                _id = uuid.value + ID.Sell.id,
                customerId = uuid.value + ID.Customer.id,
                showId = "MockData",
                customerFullName = firstName.value + " " + lastName.value,
                customerPhone = phoneNumber.value,
                showName = "MockData",
                showDate = "MockData",
                showTime = "MockData",
                showPrice = "MockData",
                showSeat = "MockData"
            )
        }

        ClientPreferences.inst.apply {
            userID = uuid.value
            userPhone = phoneNumber.value
            userFirstName = this@TicketBuyViewModel.firstName.value
            userLastName = this@TicketBuyViewModel.lastName.value
            //userAge = userAge.value
        }

        hideLoading()
        addCustomer()
    }

    fun addCustomer() {
        ioScope {
            showLoading()
            val response = forFireBaseQueries.addCustomer(customerAdd)
            if (response) {
                buyTicket()
            } else {
                errorMessage.value = message(R.string.error_server)
            }
            hideLoading()
        }
    }

    fun buyTicket() {
        ioScope {
            showLoading()
            val response = forFireBaseQueries.saveSales(sellAdd)
            if (response)
                message(R.string.success)
            else
                message(R.string.error_server)
            hideLoading()
        }

    }

    fun message(message: Int): String {
        return Resources.getSystem().getString(message)
    }
}