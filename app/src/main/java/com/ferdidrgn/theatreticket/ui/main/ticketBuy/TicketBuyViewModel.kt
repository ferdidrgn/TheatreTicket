package com.ferdidrgn.theatreticket.ui.main.ticketBuy

import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.base.BaseViewModel
import com.ferdidrgn.theatreticket.commonModels.dummyData.Customer
import com.ferdidrgn.theatreticket.commonModels.dummyData.Sell
import com.ferdidrgn.theatreticket.enums.ID
import com.ferdidrgn.theatreticket.enums.Response
import com.ferdidrgn.theatreticket.repository.CustomerFirebaseQueries
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
    private val customerFirebaseQueries: CustomerFirebaseQueries
) :
    BaseViewModel() {

    var firstName = MutableStateFlow("")
    var lastName = MutableStateFlow("")
    var phoneNumber = MutableStateFlow("")
    var age = MutableStateFlow("")
    val buyTicketPopUp = LiveEvent<Boolean?>()

    var customerAdd = Customer()
    var sellAdd = Sell()


    fun onBtnBuyTicketClick() {
        buyTicketPopUp.postValue(true)
    }

    fun checkPhoneNumber() {
        showLoading()
        mainScope {

            customerAdd = Customer(
                firstName = firstName.value,
                lastName = lastName.value,
                phoneNumber = phoneNumber.value
                //age = userAge.value
            )

            customerFirebaseQueries.checkPhoneNumber(customerAdd) { status, customerInfoList ->
                when (status) {
                    Response.Empty.response -> {
                        //Universal Unique ID
                        val id = UUID.randomUUID().toString()
                        customerAdd = Customer(
                            _id = id + ID.Customer.id,
                            firstName = firstName.value,
                            lastName = lastName.value,
                            phoneNumber = phoneNumber.value
                            //age = userAge.value
                        )
                        hideLoading()
                        fillDatas(customerAdd, true)
                    }
                    Response.ThereIs.response -> {
                        hideLoading()
                        checkTicket(customerInfoList)
                    }
                    Response.NotEqual.response -> {
                        hideLoading()
                        errorMessage.postValue(message(R.string.error_not_equal))
                    }
                    Response.ServerError.response -> {
                        hideLoading()
                        errorMessage.postValue(message(R.string.error_server))
                    }
                }

            }
        }
    }

    private fun checkTicket(customer: Customer?) {

        showLoading()
        mainScope {
            sellFirebaseQueries.checkBuyTicketCustomerId(customer?._id.toString()) { status ->
                when (status) {
                    Response.Empty.response -> {
                        hideLoading()
                        fillDatas(customer, false)
                    }
                    Response.ThereIs.response -> {
                        hideLoading()
                        errorMessage.postValue(message(R.string.error_have_ticket))
                    }
                    Response.ServerError.response -> {
                        hideLoading()
                        errorMessage.postValue(message(R.string.error_server))
                    }
                }
            }
        }
    }

    private fun fillDatas(customer: Customer?, isNewCustom: Boolean) {

        showLoading()

        mainScope {
            customerAdd = Customer(
                _id = customer?._id.toString(),
                firstName = customer?.firstName.toString(),
                lastName = customer?.lastName.toString(),
                phoneNumber = customer?.phoneNumber.toString()
                //age = userAge.value
            )

            val id = UUID.randomUUID().toString()
            sellAdd = Sell(
                _id = id + ID.Sell.id,
                customerId = customer?._id.toString(),
                showId = "MockData",
                customerFullName = customer?.firstName.toString() + " " + customer?.lastName.toString(),
                customerPhone = customer?.phoneNumber.toString(),
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
        if (isNewCustom) addCustomer() else buyTicket()
    }

    private fun addCustomer() {
        showLoading()
        mainScope {
            customerFirebaseQueries.addCustomer(customerAdd) { status ->
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
}