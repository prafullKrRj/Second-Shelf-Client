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
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.prafull.secondshelf.mainApp.ui.MainApp
import com.prafull.secondshelf.onBoard.OnBoardingStartingScreen
import com.prafull.secondshelf.onBoard.login.LoginScreen
import com.prafull.secondshelf.onBoard.register.RegisterScreen
import com.prafull.secondshelf.ui.theme.SecondShelfTheme
import com.prafull.secondshelf.utils.SharedPrefManager
import org.koin.androidx.compose.getViewModel
import org.koin.compose.koinInject

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
                        composable<Routes.MainApp> {
                            MainApp(getViewModel())
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