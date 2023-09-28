package com.ferdidrgn.theatreticket.commonModels

import com.google.gson.annotations.SerializedName

data class Stage(
    @SerializedName("createdAt")
    var _createdAt: String? = "",
    @SerializedName("id")
    var _id: String? = "",
    @SerializedName("name")
    var name: String? = "",
    @SerializedName("description")
    var description: String? = "",
    @SerializedName("capacity")
    var capacity: String? = "",
    @SerializedName("location")
    var location: String? = "",
    @SerializedName("lat")
    val lat: Double?,
    @SerializedName("long")
    val long: Double?,
    @SerializedName("communication")
    var communication: String? = "",
    @SerializedName("seatId")
    var seatId: ArrayList<String?>? = null,
    @SerializedName("showsId")
    var showsId: ArrayList<String?>? = null,
)