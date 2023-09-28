package com.ferdidrgn.theatreticket.commonModels

import com.google.gson.annotations.SerializedName

data class Seat(
    @SerializedName("createdAt")
    var _createdAt: String? = "",
    @SerializedName("id")
    var _id: String? = "",
    @SerializedName("statu")
    var _statu: Boolean?,
    @SerializedName("no")
    var no: String? = "",
    @SerializedName("stageId")
    var stageId: String? = "",
    @SerializedName("customBuyId")
    var customBuyId: String? = "",
    @SerializedName("buyDate")
    var buyDate: String? = "",
    )
