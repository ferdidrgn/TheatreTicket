package com.ferdidrgn.theatreticket.base

import com.google.gson.annotations.SerializedName

data class BaseReponse<T>(
    @SerializedName("data")
    val `data`: T,
    @SerializedName("err")
    val err: Err,
    @SerializedName("isSuccess")
    val isSuccess: Boolean
)

data class Err(
    @SerializedName("message")
    val message: String? = "",
    @SerializedName("code")
    val code: Int? = null,
)