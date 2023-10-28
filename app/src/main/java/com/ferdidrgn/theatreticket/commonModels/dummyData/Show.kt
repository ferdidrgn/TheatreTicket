package com.ferdidrgn.theatreticket.commonModels.dummyData

import com.ferdidrgn.theatreticket.base.ListAdapterItem
import com.ferdidrgn.theatreticket.commonModels.Players
import com.ferdidrgn.theatreticket.commonModels.Stage
import java.io.Serializable

data class Show(
    var _createdAt: String? = null,
    var _id: String? = null,
    var name: String? = null,
    var description: String? = null,
    var imageUrl: String? = null,
    var date: String? = null,
    var price: String? = null,
    var ageLimit: String? = null,
    var seat: String? = null,
    var stageId: ArrayList<Stage?>? = null,
    var actorsId: ArrayList<Players?>? = null,
    override val mId: Long = 1L
) : ListAdapterItem, Serializable