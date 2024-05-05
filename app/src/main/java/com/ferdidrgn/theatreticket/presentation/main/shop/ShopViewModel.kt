package com.ferdidrgn.theatreticket.presentation.main.shop

import com.ferdidrgn.theatreticket.util.base.BaseViewModel
import com.ferdidrgn.theatreticket.data.repository.SellFirebaseQueries
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShopViewModel @Inject constructor(
    private val sellFirebaseQueries: SellFirebaseQueries
) : BaseViewModel() {


}