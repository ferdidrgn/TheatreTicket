package com.ferdidrgn.theatreticket.ui.main.ticketSearch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ferdidrgn.theatreticket.base.BaseViewModel
import com.ferdidrgn.theatreticket.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class TicketSearchViewModel @Inject constructor(context: ProductRepository) : BaseViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    val text: LiveData<String> = _text
}