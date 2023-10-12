package com.ferdidrgn.theatreticket.ui.main.ticketSearch

import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.base.BaseViewModel
import com.ferdidrgn.theatreticket.commonModels.dummyData.Customer
import com.ferdidrgn.theatreticket.commonModels.dummyData.Sell
import com.ferdidrgn.theatreticket.enums.Response
import com.ferdidrgn.theatreticket.forFirebaseQueries.ForFirebaseQueries
import com.ferdidrgn.theatreticket.tools.helpers.LiveEvent
import com.ferdidrgn.theatreticket.tools.mainScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject


@HiltViewModel
class TicketSearchViewModel @Inject constructor(private val forFirebaseQueries: ForFirebaseQueries) :
    BaseViewModel(), FindTicketAdapterListener {

    var firstName = MutableStateFlow("")
    var lastName = MutableStateFlow("")
    var fullName = MutableStateFlow("")
    var phoneNumber = MutableStateFlow("")
    var _createdAt = MutableStateFlow("")
    var _id = MutableStateFlow("")
    var customerId = MutableStateFlow("")
    var showId = MutableStateFlow("")
    var showName = MutableStateFlow("")
    var showDate = MutableStateFlow("")
    var showTime = MutableStateFlow("")
    var showPrice = MutableStateFlow("")
    var showSeat = MutableStateFlow("")
    var stageName = MutableStateFlow("")
    var stageLocation = MutableStateFlow("")
    val errorMessage = LiveEvent<String?>()
    val successMessage = LiveEvent<String?>()
    val searchTicketPopUp = LiveEvent<Boolean?>()

    var customerAdd = Customer()
    val sell = MutableStateFlow<List<Sell>>(listOf())


    fun onBtnSearchTicketClick() {
        searchTicketPopUp.postValue(true)
    }

    fun searchTicket() {
        showLoading()
        mainScope {
            customerAdd = Customer(
                firstName = firstName.value,
                lastName = lastName.value,
                phoneNumber = phoneNumber.value
            )

            forFirebaseQueries.checkSearchBuyTicket(customerAdd) { status ->
                when (status) {
                    Response.ServerError.response -> {
                        errorMessage.postValue(message(R.string.error_server))
                        hideLoading()
                    }
                    Response.Empty.response -> {
                        errorMessage.postValue(message(R.string.error_no_any_ticket))
                        hideLoading()
                    }
                    Response.ThereIs.response -> {
                        successMessage.postValue(message(R.string.success_ticket))
                        hideLoading()
                    }
                }
            }
        }
    }

    override fun onFindTicketItemClicked(position: Int) {
        //location
    }
}