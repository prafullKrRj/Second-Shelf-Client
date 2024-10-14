package com.prafull.secondshelf.onBoard.model

import com.google.gson.annotations.SerializedName

data class GeneralResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String
)