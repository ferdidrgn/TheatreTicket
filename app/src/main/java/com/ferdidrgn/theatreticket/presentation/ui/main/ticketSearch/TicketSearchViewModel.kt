package com.ferdidrgn.theatreticket.presentation.ui.main.ticketSearch

import androidx.lifecycle.MutableLiveData
import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.base.BaseViewModel
import com.ferdidrgn.theatreticket.domain.model.commonModels.dummyData.User
import com.ferdidrgn.theatreticket.domain.model.commonModels.dummyData.Sell
import com.ferdidrgn.theatreticket.tools.enums.Response
import com.ferdidrgn.theatreticket.data.repository.SellFirebaseQueries
import com.ferdidrgn.theatreticket.tools.helpers.LiveEvent
import com.ferdidrgn.theatreticket.tools.mainScope
import com.ferdidrgn.theatreticket.tools.removeWhiteSpace
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject


@HiltViewModel
class TicketSearchViewModel @Inject constructor(private val sellFirebaseQueries: SellFirebaseQueries) :
    BaseViewModel(), FindTicketAdapterListener {

    var firstName = MutableStateFlow("")
    var lastName = MutableStateFlow("")
    var phoneNumber = MutableStateFlow("")
    val searchTicketPopUp = LiveEvent<Boolean?>()

    var userAdd = User()
    val sell = MutableLiveData<List<Sell?>?>()


    fun onBtnSearchTicketClick() {
        searchTicketPopUp.postValue(true)
    }

    fun searchTicket() {
        showLoading()
        mainScope {
            userAdd = User(
                firstName = firstName.value,
                lastName = lastName.value,
                phoneNumber = phoneNumber.value.removeWhiteSpace()
            )

            sellFirebaseQueries.checkSearchBuyTicket(userAdd) { status, sellList ->
                when (status) {
                    Response.ServerError -> {
                        errorMessage.postValue(message(R.string.error_server))
                        hideLoading()
                    }
                    Response.Empty -> {
                        errorMessage.postValue(message(R.string.error_no_any_ticket))
                        hideLoading()
                    }
                    Response.ThereIs -> {
                        successMessage.postValue(message(R.string.success_ticket))
                        sellList.let {
                            sell.postValue(it)
                        }
                        hideLoading()
                    }
                    else -> {
                        errorMessage.postValue(message(R.string.error))
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

        if (phoneNumber.value.removeWhiteSpace().length != 13) {
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