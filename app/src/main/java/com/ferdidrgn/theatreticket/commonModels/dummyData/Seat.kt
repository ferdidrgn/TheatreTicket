package com.ferdidrgn.theatreticket.commonModels.dummyData

import com.ferdidrgn.theatreticket.base.ListAdapterItem

data class Seat(
    var _createdAt: String? = null,
    var _id: String? = null,
    var _statu: Boolean? = null,
    var name: String? = null,
    var isSelected: Boolean? = null,
    var stageId: String? = null,

    override val mId: Long = 1L
) : ListAdapterItem {

    //took isSelected for marking one item as selected or not
    fun setIsSelected(value: Boolean) {
        isSelected = value
    }
}