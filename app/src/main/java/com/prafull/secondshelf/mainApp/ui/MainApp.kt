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
import androidx.navigation.NavController
import com.prafull.secondshelf.R
import com.prafull.secondshelf.mainApp.screens.books.BookScreen
import com.prafull.secondshelf.mainApp.screens.home.HomeScreen
import com.prafull.secondshelf.mainApp.screens.list.ListingScreen
import com.prafull.secondshelf.mainApp.screens.profile.UserProfileScreen
import com.prafull.secondshelf.model.User
import org.koin.androidx.compose.getViewModel

@Composable
fun MainApp(navController: NavController, viewModel: MainViewModel, logout: () -> Unit = {}) {
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
                HomeScreen(viewModel = getViewModel(), navController)
            }

            AppDestinations.Books -> {
                BookScreen(viewModel = getViewModel(), navController)
            }

            AppDestinations.Listing -> {
                ListingScreen(viewModel = getViewModel(), navController)
            }

            AppDestinations.Profile -> {
                UserProfileScreen(
                    user = User(
                        username = "prafull",
                        password = "123456",
                        fullName = "Prafull Kumar",
                        mobileNumber = "1234567890"
                    )
                ) {

                }
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
    @DrawableRes val selectedIcon: Int,
    @DrawableRes val unSelectedIcon: Int,
    val title: String
) {
    Home(
        R.drawable.baseline_home_24,
        R.drawable.outline_home_24,
        "Home"
    ),
    Books(
        R.drawable.baseline_library_books_24,
        R.drawable.outline_library_books_24,
        "Books"
    ),
    Listing(
        R.drawable.outline_list_24,
        R.drawable.outline_list_24,
        "Listing"
    ),
    Profile(
        R.drawable.baseline_person_24,
        R.drawable.outline_person_24,
        "Profile"
    ),
    Settings(
        R.drawable.baseline_settings_24,
        R.drawable.outline_settings_24,
        "Settings"
    )
}