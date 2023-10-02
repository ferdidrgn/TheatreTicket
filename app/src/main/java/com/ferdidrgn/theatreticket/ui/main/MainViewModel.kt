package com.ferdidrgn.theatreticket.ui.main

import android.content.Context
import com.ferdidrgn.theatreticket.base.BaseViewModel
import com.ferdidrgn.theatreticket.di.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(context: ProductRepository) : BaseViewModel() {
}