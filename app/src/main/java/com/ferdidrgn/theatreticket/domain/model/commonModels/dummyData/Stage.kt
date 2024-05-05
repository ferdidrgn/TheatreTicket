package com.ferdidrgn.theatreticket.domain.model.commonModels.dummyData

import android.net.Uri
import com.ferdidrgn.theatreticket.base.ListAdapterItem
import java.io.Serializable

data class Stage(
    var _createdAt: String? = null,
    var _id: String? = null,
    var name: String? = null,
    var imgUrl: String? = null,
    var addOrUpdateImgUrl: Uri? = null,
    var description: String? = null,
    var communication: String? = null,
    var capacity: String? = null,
    var address: String? = null,
    val locationLat: Double? = null,
    val locationLng: Double? = null,
    val seatId: String? = null,
    var seatColumnCount: Int? = null,
    var seatRowCount: Int? = null,

    override val mId: Long = 1,
) : ListAdapterItem, Serializable
