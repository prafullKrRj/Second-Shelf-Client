package com.prafull.secondshelf.onBoard.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class Book(
    @SerializedName("title") val title: String,
    @SerializedName("author") val author: String,
    @SerializedName("yearOfPrinting") val yearOfPrinting: Int?,
    @SerializedName("description") val description: String?,
    @SerializedName("genre") val genre: String?,
    @SerializedName("coverImageUrl") val coverImageUrl: String?,
    @SerializedName("numberOfPages") val numberOfPages: Int?,
    @SerializedName("price") val price: Double,
    @SerializedName("listedAt") val listedAt: LocalDateTime = LocalDateTime.now(),
    @SerializedName("id") val id: Long? = 0
)