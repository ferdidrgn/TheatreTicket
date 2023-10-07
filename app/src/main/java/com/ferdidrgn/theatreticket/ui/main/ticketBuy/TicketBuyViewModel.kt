package com.ferdidrgn.theatreticket.ui.main.ticketBuy

import androidx.lifecycle.MutableLiveData
import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.base.BaseViewModel
import com.ferdidrgn.theatreticket.commonModels.dummyData.Customer
import com.ferdidrgn.theatreticket.commonModels.dummyData.Sell
import com.ferdidrgn.theatreticket.enums.ID
import com.ferdidrgn.theatreticket.enums.Response
import com.ferdidrgn.theatreticket.forFirebaseQueries.ForFirebaseQueries
import com.ferdidrgn.theatreticket.tools.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.UUID
import javax.inject.Inject
import com.ferdidrgn.theatreticket.tools.helpers.LiveEvent
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@HiltViewModel
class TicketBuyViewModel @Inject constructor(private val forFireBaseQueries: ForFirebaseQueries) :
    BaseViewModel() {

    private val fireStore = Firebase.firestore
    var customerId = MutableLiveData<String?>()
    var statusTree = MutableLiveData<Boolean?>()


    var firstName = MutableStateFlow("")
    var lastName = MutableStateFlow("")
    var phoneNumber = MutableStateFlow("")
    var age = MutableStateFlow("")
    val buyTicketPopUp = LiveEvent<Boolean?>()
    val errorMessage = LiveEvent<String?>()

    var customerAdd = Customer()
    var sellAdd = Sell()

    fun onBtnBuyTicketClick() {
        var isRequiredFieldsDone = true

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
        //phoneNumber.collectLatest {
        /*if (phoneNumber.value.length < 10) {
            isRequiredFieldsDone = false
            errorMessage.postValue(message(R.string.error_phone_little))
        }*/
        // }
        if (isRequiredFieldsDone) {
            buyTicketPopUp.postValue(true)
        }
    }

    fun checkPhoneNumber() {

        mainScope {
            showLoading()
            forFireBaseQueries.checkPhoneNumber(phoneNumber.value) { response ->
                when (response.second) {
                    Response.ServerError.response -> {
                        errorMessage.postValue(message(R.string.error_server))
                        hideLoading()
                    }

                    Response.ThereIs.response -> {
                        forFireBaseQueries.checkBuyTicket(response.first) { status ->
                            when (status) {
                                Response.ServerError.response -> {
                                    errorMessage.postValue(message(R.string.error_server))
                                    hideLoading()
                                }

                                Response.ThereIs.response -> {
                                    errorMessage.postValue(message(R.string.error_have_ticket))
                                    hideLoading()
                                }

                                Response.Empty.response -> {
                                    hideLoading()
                                    checkPhoneResume(response.first)
                                }

                            }
                        }
                    }

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
                        checkPhoneResume(id + ID.Customer.id)
                    }

                    else -> {
                        hideLoading()
                        errorMessage.postValue(message(R.string.error_server))
                    }
                }
            }
        }
    }


    private fun checkPhoneResume(customerId: String?) {

        showLoading()

        sellAdd = Sell(
            _id = customerId + ID.Sell.id,
            customerId = customerId,
            showId = "MockData",
            customerFullName = firstName.value + " " + lastName.value,
            customerPhone = phoneNumber.value,
            showName = "MockData",
            showDate = "MockData",
            showTime = "MockData",
            showPrice = "MockData",
            showSeat = "MockData"
        )

        ClientPreferences.inst.apply {
            userID = customerId + ID.Customer.id
            userPhone = phoneNumber.value
            userFirstName = this@TicketBuyViewModel.firstName.value
            userLastName = this@TicketBuyViewModel.lastName.value
            //userAge = userAge.value
        }

        hideLoading()
        addCustomer()
    }

    fun addCustomer() {
        showLoading()
        forFireBaseQueries.addCustomer(customerAdd) { status ->
            if (status) {
                hideLoading()
                buyTicket()
            } else {
                hideLoading()
                errorMessage.postValue(message(R.string.error_server))
            }
        }
    }

    fun buyTicket() {
        showLoading()
        forFireBaseQueries.saveSales(sellAdd) { status ->
            if (status) {
                hideLoading()
                errorMessage.postValue(message(R.string.success)) //NOT: error mesaja success yazdık düzelt
            } else {
                hideLoading()
                errorMessage.postValue(message(R.string.error_server))
            }
        }
    }

    private fun message(message: Int): String {
        return getContext().let {
            it?.resources?.getString(message).toString()
        }
    }
}