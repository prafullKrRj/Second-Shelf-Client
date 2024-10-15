package com.prafull.secondshelf

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.prafull.secondshelf.mainApp.screens.commons.BookDetailsScreen
import com.prafull.secondshelf.mainApp.screens.home.searchScreen.SearchScreen
import com.prafull.secondshelf.mainApp.ui.MainApp
import com.prafull.secondshelf.onBoard.OnBoardingStartingScreen
import com.prafull.secondshelf.onBoard.login.LoginScreen
import com.prafull.secondshelf.onBoard.register.RegisterScreen
import com.prafull.secondshelf.ui.theme.SecondShelfTheme
import com.prafull.secondshelf.utils.SharedPrefManager
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SecondShelfTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .systemBarsPadding()
                ) { innerPadding ->
                    val navController = rememberNavController()
                    val prefManager = koinInject<SharedPrefManager>()
                    val sd =
                        if (prefManager.isLoggedIn()) Routes.MainApp else Routes.OnBoard
                    NavHost(
                        modifier = Modifier.padding(innerPadding),
                        navController = navController,
                        startDestination = sd
                    ) {
                        onBoardGraph(navController)
                        navigation<Routes.MainApp>(startDestination = MainAppRoutes.HomeScreen) {
                            composable<MainAppRoutes.HomeScreen> {
                                MainApp(navController, getViewModel())
                            }
                            composable<MainAppRoutes.SearchScreen> {
                                SearchScreen(
                                    koinViewModel { parametersOf(it.toRoute<MainAppRoutes.SearchScreen>().initialSearchQuery) },
                                    navController
                                )
                            }
                            composable<MainAppRoutes.BookDetailsScreen> {
                                BookDetailsScreen(
                                    book = it.toRoute<MainAppRoutes.BookDetailsScreen>().toBook(),
                                    navController = navController
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

fun NavGraphBuilder.onBoardGraph(navController: NavController) {
    navigation<Routes.OnBoard>(startDestination = OnBoardRoutes.Start) {
        composable<OnBoardRoutes.Register> {
            RegisterScreen(registerViewModel = getViewModel(), navController)
        }
        composable<OnBoardRoutes.Start> {
            OnBoardingStartingScreen(navController)
        }
        composable<OnBoardRoutes.Login> {
            LoginScreen(loginViewModel = getViewModel(), navController)
        }
    }
}

fun NavController.clearBackstackAndNavigate(route: Any) {
    val navOptions = NavOptions.Builder()
        .setPopUpTo(graph.startDestinationId, inclusive = true)
        .build()
    navigate(route, navOptions)
}

fun NavController.goBackStack() {
    if (currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
        popBackStack()
    }
}