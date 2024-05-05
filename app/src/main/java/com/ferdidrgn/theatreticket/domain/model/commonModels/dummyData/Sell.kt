package com.ferdidrgn.theatreticket.domain.model.commonModels.dummyData

import com.ferdidrgn.theatreticket.util.base.ListAdapterItem

data class Sell(
    var _createdAt: String? = null,
    var _id: String? = null,
    var customerId: String? = null,
    var customerFullName: String? = null,
    var customerPhone: String? = null,
    var showId: String? = null,
    var showName: String? = null,
    var showDate: String? = null,
    var showTime: String? = null,
    var showPrice: String? = null,
    var showSeat: String? = null,
    var stageName: String? = null,
    var stageLocation: String? = null,
    var locationLat: String? = null,
    var locationLng: String? = null,
    override val mId: Long = 1L
) : ListAdapterItem