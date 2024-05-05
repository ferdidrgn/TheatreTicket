package com.ferdidrgn.theatreticket.domain.model

import com.google.gson.annotations.SerializedName

data class Show(
    @SerializedName("_createdAt")
    var _createdAt: String? = "",
    @SerializedName("_id")
    var _id: String? = "",
    @SerializedName("name")
    var name: String? = "",
    @SerializedName("description")
    var description: String? = "",
    @SerializedName("imgUrl")
    var imageUrl: String? = null,
    @SerializedName("date")
    var date: ArrayList<String>?,
    @SerializedName("price")
    var price: String? = "",
    @SerializedName("ageLimit")
    var ageLimit: String? = "",
    @SerializedName("actorsId")
    var actors: ArrayList<Players?>? = null,
)