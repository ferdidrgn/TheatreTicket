package com.ferdidrgn.theatreticket.ui.main.shop

import com.ferdidrgn.theatreticket.base.BaseViewModel
import com.ferdidrgn.theatreticket.repository.UserFirebaseQueries
import com.ferdidrgn.theatreticket.repository.SellFirebaseQueries
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShopViewModel @Inject constructor(
    private val sellFirebaseQueries: SellFirebaseQueries,
    private val userFirebaseQueries: UserFirebaseQueries
) :
    BaseViewModel() {


}