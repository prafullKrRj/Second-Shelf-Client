package com.prafull.secondshelf.mainApp.ui

import androidx.annotation.DrawableRes
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.compose.rememberNavController
import com.prafull.secondshelf.MainAppRoutes
import com.prafull.secondshelf.R
import com.prafull.secondshelf.mainApp.screens.home.HomeScreen
import com.prafull.secondshelf.mainApp.screens.home.HomeViewModel
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainApp(viewModel: MainViewModel, logout: () -> Unit = {}) {
    val navController = rememberNavController()
    var currentDestination by rememberSaveable {
        mutableStateOf(AppDestinations.Home)
    }

    NavigationSuiteScaffold(navigationSuiteItems = {
        AppDestinations.entries.forEach {
            item(icon = {
                Icon(
                    imageVector = ImageVector.vectorResource(id = if (currentDestination == it) it.selectedIcon else it.unSelectedIcon),
                    contentDescription = it.title
                )
            },
                label = { Text(it.title) },
                selected = it == currentDestination,
                onClick = { currentDestination = it })
        }
    }) {
        when (currentDestination) {
            AppDestinations.Home -> {
                HomeScreen(viewModel = getViewModel())
            }

            AppDestinations.Books -> {
                Text(text = "Books")
            }

            AppDestinations.Listing -> {
                Text(text = "Listing")
            }

            AppDestinations.Profile -> {
                Text(text = "pROFILE")
            }

            AppDestinations.Settings -> {

            }
        }
    }
}

enum class Models {
    HOME, LISTING, BOOKS, PROFILE, SETTINGS
}

enum class AppDestinations(
    val route: Any,
    @DrawableRes val selectedIcon: Int,
    @DrawableRes val unSelectedIcon: Int,
    val title: String
) {
    Home(
        MainAppRoutes.HomeScreen,
        R.drawable.baseline_home_24,
        R.drawable.outline_home_24,
        "Home"
    ),
    Books(
        MainAppRoutes.BooksScreen,
        R.drawable.baseline_library_books_24,
        R.drawable.outline_library_books_24,
        "Books"
    ),
    Listing(
        MainAppRoutes.ListingScreen,
        R.drawable.outline_list_24,
        R.drawable.outline_list_24,
        "Listing"
    ),
    Profile(
        MainAppRoutes.ProfileScreen,
        R.drawable.baseline_person_24,
        R.drawable.outline_person_24,
        "Profile"
    ),
    Settings(
        MainAppRoutes.SettingsScreen,
        R.drawable.baseline_settings_24,
        R.drawable.outline_settings_24,
        "Settings"
    )
}