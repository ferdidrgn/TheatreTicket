package com.ferdidrgn.theatreticket.commonModels.dummyData

import android.net.Uri
import com.ferdidrgn.theatreticket.base.ListAdapterItem
import com.ferdidrgn.theatreticket.commonModels.Players
import java.io.Serializable

data class Show(
    var _createdAt: String? = null,
    var _id: String? = null,
    var name: String? = null,
    var description: String? = null,
    var imageUrl: String? = null,
    var addOrUpdateImgUrl: Uri? = null,
    var date: String? = null,
    var time: String? = null,
    var price: String? = null,
    var ageLimit: String? = null,
    var seatId: String? = null,
    var stageId: ArrayList<Any>? = null,
    var actorsId: ArrayList<Players?>? = null,
    override val mId: Long = 1L
) : ListAdapterItem, Serializable