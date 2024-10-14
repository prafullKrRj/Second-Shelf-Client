package com.prafull.secondshelf

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
    data object SettingsScreen : MainAppRoutes

    @Serializable
    data object ListingScreen : MainAppRoutes

    @Serializable
    data object ProfileScreen : MainAppRoutes

    @Serializable
    data object BooksScreen : MainAppRoutes
}