package com.prafull.secondshelf.model

import com.google.gson.annotations.SerializedName

data class Transaction(
    @SerializedName("transactionId") val transactionId: Long,
    @SerializedName("bookId") val bookId: Long,
    @SerializedName("amount") val amount: Double,
    @SerializedName("transactionDate") val transactionDate: Long? = null,
    @SerializedName("sellerUserName") val sellerUserName: String,
    @SerializedName("buyerUserName") val buyerUserName: String
)