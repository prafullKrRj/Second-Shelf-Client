package com.prafull.secondshelf

import com.prafull.secondshelf.model.Book
import kotlinx.serialization.Serializable


sealed interface OnBoardRoutes {
    @Serializable
    data object Start : MainAppRoutes

    @Serializable
    data object Register : MainAppRoutes

    @Serializable
    data object Login : MainAppRoutes
}

sealed interface Routes {
    @Serializable
    data object OnBoard : Routes

    @Serializable
    data object MainApp : Routes
}

sealed interface MainAppRoutes {


    @Serializable
    data object HomeScreen : MainAppRoutes

    @Serializable
    data class SearchScreen(
        val initialSearchQuery: String = ""
    ) : MainAppRoutes

    @Serializable
    data class BookDetailsScreen(
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
        val sellerNumber: String,
        val sellerFullName: String? = null
    ) : MainAppRoutes {
        fun toBook() = Book(
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