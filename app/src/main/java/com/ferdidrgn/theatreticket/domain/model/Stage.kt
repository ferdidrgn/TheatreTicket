package com.ferdidrgn.theatreticket.domain.model

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
    val locationLat: Double?,
    @SerializedName("long")
    val locationLng: Double?,
    @SerializedName("communication")
    var communication: String? = "",
    @SerializedName("seatId")
    var seat: ArrayList<Seat?>? = null,
    @SerializedName("showsId")
    var shows: ArrayList<Show?>? = null,
)