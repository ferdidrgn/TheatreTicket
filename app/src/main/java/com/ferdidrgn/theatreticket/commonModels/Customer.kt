package com.ferdidrgn.theatreticket.commonModels

import com.google.gson.annotations.SerializedName

data class Customer(
    @SerializedName("createdAt")
    var _createdAt: String? = "",
    @SerializedName("id")
    var _id: String? = "",
    @SerializedName("firstName")
    var firstName: String? = "",
    @SerializedName("lastName")
    var lastName: String? = "",
    @SerializedName("phoneNumber")
    var phoneNumber: String? = "",
    @SerializedName("isActivite")
    var isActivite: String? = "",
    @SerializedName("age")
    var age: String? = "",
    @SerializedName("eMail")
    var eMail: String? = "",
    @SerializedName("ticketId")
    var ticketId: ArrayList<String?>? = null,
)