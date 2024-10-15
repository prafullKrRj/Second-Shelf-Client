package com.prafull.secondshelf.model

import com.google.gson.annotations.SerializedName

data class GeneralResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String
)