package com.ferdidrgn.theatreticket.domain.model

import com.google.gson.annotations.SerializedName

data class Players(
    @SerializedName("createdAt")
    var _createdAt: String? = "",
    @SerializedName("id")
    var _id: String? = "",
    @SerializedName("firstName")
    var firstName: String? = "",
    @SerializedName("lastName")
    var lastName: String? = "",
    @SerializedName("gameId")
    var gameId: ArrayList<String?>? = null,
)
