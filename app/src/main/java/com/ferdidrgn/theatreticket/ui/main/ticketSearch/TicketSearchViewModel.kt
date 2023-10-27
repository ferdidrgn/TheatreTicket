package com.ferdidrgn.theatreticket.ui.main.ticketSearch

import androidx.lifecycle.MutableLiveData
import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.base.BaseViewModel
import com.ferdidrgn.theatreticket.commonModels.dummyData.Customer
import com.ferdidrgn.theatreticket.commonModels.dummyData.Sell
import com.ferdidrgn.theatreticket.enums.Response
import com.ferdidrgn.theatreticket.repository.SellFirebaseQueries
import com.ferdidrgn.theatreticket.tools.helpers.LiveEvent
import com.ferdidrgn.theatreticket.tools.mainScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject


@HiltViewModel
class TicketSearchViewModel @Inject constructor(private val sellFirebaseQueries: SellFirebaseQueries) :
    BaseViewModel(), FindTicketAdapterListener {

    var firstName = MutableStateFlow("")
    var lastName = MutableStateFlow("")
    var phoneNumber = MutableStateFlow("")
    var _createdAt = MutableStateFlow("")
    var _id = MutableStateFlow("")
    val searchTicketPopUp = LiveEvent<Boolean?>()

    var customerAdd = Customer()
    val sell = MutableLiveData<List<Sell?>?>()


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

            sellFirebaseQueries.checkSearchBuyTicket(customerAdd) { status, sellList ->
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
                        sellList.let {
                            sell.postValue(it)
                        }
                        hideLoading()
                    }
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

        if (phoneNumber.value.length != 12) {
            isRequiredFieldsDone = false
            message = message(R.string.error_phone_little)
        }

        if (isRequiredFieldsDone) {
            searchTicket()
        } else
            errorMessage.postValue(message)
    }

    override fun onFindTicketItemClicked(position: Int) {
        //location
    }
}