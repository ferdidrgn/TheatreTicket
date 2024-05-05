package com.ferdidrgn.theatreticket.domain.model.commonModels.dummyData

import com.ferdidrgn.theatreticket.base.ListAdapterItem

data class SellBox(
    var _createdAt: String? = null,
    var _id: String? = null,
    var stage: Stage? = null,
    var show: Show? = null,
    var sell: Sell? = null,
    var user: User? = null,
    var seat: Seat? = null,
    var chooseSeat : ArrayList<String?>? = null,
    override val mId: Long = 1L
) : ListAdapterItem