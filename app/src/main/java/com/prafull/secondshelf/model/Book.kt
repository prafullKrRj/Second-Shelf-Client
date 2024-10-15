package com.prafull.secondshelf.model

import com.prafull.secondshelf.MainAppRoutes

data class Book(
    val author: String,
    val coverImageUrl: String,
    val description: String,
    val genre: String,
    val id: Int,
    val listedAt: Long,
    val numberOfPages: Int,
    val price: Double,
    val title: String,
    val yearOfPrinting: Int,
    val sellerUserName: String? = null,
    val sellerNumber: String = "123456789",
    val sellerFullName: String? = null
) {
    fun toBookScreen(): MainAppRoutes.BookDetailsScreen {
        return MainAppRoutes.BookDetailsScreen(
            author,
            coverImageUrl,
            description,
            genre,
            id,
            listedAt,
            numberOfPages,
            price,
            title,
            yearOfPrinting,
            sellerUserName, sellerNumber, sellerFullName
        )
    }
}