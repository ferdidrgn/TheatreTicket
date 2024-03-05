package com.ferdidrgn.theatreticket.commonModels

import com.google.gson.annotations.SerializedName

data class Sell(
    @SerializedName("createdAt")
    var _createdAt: String? = "",
    @SerializedName("id")
    var _id: String? = "",
    @SerializedName("customerId")
    var customerId: String? = null,
    @SerializedName("showId")
    var showId: String? = null,
    @SerializedName("customer")
    var customer: String? = null,
)