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
    )
