package com.ferdidrgn.theatreticket.domain.model.commonModels.dummyData

import com.ferdidrgn.theatreticket.util.base.ListAdapterItem

data class Seat(
    var _createdAt: String? = null,
    var _id: String? = null,
    var stageId: String? = null,

    override val mId: Long = 1L
) : ListAdapterItem