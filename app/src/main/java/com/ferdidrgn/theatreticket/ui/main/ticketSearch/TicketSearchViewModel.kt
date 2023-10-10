package com.ferdidrgn.theatreticket.ui.main.ticketSearch

import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.base.BaseViewModel
import com.ferdidrgn.theatreticket.commonModels.dummyData.Customer
import com.ferdidrgn.theatreticket.enums.Response
import com.ferdidrgn.theatreticket.forFirebaseQueries.ForFirebaseQueries
import com.ferdidrgn.theatreticket.tools.helpers.LiveEvent
import com.ferdidrgn.theatreticket.tools.mainScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject


@HiltViewModel
class TicketSearchViewModel @Inject constructor(private val forFirebaseQueries: ForFirebaseQueries) :
    BaseViewModel() {

    var firstName = MutableStateFlow("")
    var lastName = MutableStateFlow("")
    var phoneNumber = MutableStateFlow("")
    val errorMessage = LiveEvent<String?>()
    val successMessage = LiveEvent<String?>()

    var customerAdd = Customer()


    init {
        customerAdd = Customer(
            firstName = firstName.value,
            lastName = lastName.value,
            phoneNumber = phoneNumber.value
        )
    }

    fun searchTicket() {
        showLoading()
        mainScope {
            forFirebaseQueries.checkSearchBuyTicket(customerAdd) { status, sellData ->
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
                        //itemları tek tek alacağız
                        hideLoading()
                    }
                }
            }
        }
    }
}