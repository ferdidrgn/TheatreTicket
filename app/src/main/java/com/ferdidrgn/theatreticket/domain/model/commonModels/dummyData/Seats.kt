package com.ferdidrgn.theatreticket.domain.model.commonModels.dummyData

import com.ferdidrgn.theatreticket.util.base.ListAdapterItem

data class Seats(
    var _createdAt: String? = null,
    var _id: String? = null,
    var seatId: String? = null,
    var name: String? = null,
    var isEmpty: Boolean? = null,
    override val mId: Long = 1L
) : ListAdapterItem {

    //took isSelected for marking one item as selected or not
    fun setIsSelected(value: Boolean) {
        isEmpty = value
    }
}