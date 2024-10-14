package com.prafull.secondshelf.onBoard.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class Transaction(
    @SerializedName("book_id") val bookId: Long,
    @SerializedName("amount") val amount: Double,
    @SerializedName("transactionDate") val transactionDate: LocalDateTime = LocalDateTime.now(),
    @SerializedName("sellerUserName") val sellerUserName: String,
    @SerializedName("buyerUserName") val buyerUserName: String
)