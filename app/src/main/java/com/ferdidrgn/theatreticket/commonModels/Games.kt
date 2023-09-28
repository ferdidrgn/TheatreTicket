package com.ferdidrgn.theatreticket.commonModels

import com.google.gson.annotations.SerializedName

data class Games(
    @SerializedName("createdAt")
    var _createdAt: String? = "",
    @SerializedName("id")
    var _id: String? = "",
    @SerializedName("name")
    var name: String? = "",
    @SerializedName("description")
    var description: String? = "",
    @SerializedName("date")
    var date: String? = "",
    @SerializedName("price")
    var price: String? = "",
    @SerializedName("ageLimit")
    var ageLimit: String? = "",
    @SerializedName("stageId")
    var stageId: ArrayList<Stage?>? = null,
    @SerializedName("actorsId")
    var actorsId: ArrayList<String?>? = null,
)