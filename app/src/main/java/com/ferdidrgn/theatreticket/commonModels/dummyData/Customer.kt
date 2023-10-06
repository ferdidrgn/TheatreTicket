package com.ferdidrgn.theatreticket.commonModels.dummyData

import com.google.gson.annotations.SerializedName

data class Customer(
    var _createdAt: String = "",
    var _id: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var phoneNumber: String = "",
    var isActivite: String = "",
    var age: String = "",
    var eMail: String = ""
)