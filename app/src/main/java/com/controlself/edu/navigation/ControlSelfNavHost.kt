package com.controlself.edu.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.controlself.edu.ui.screens.common.RoleHomeStubScreen
import com.controlself.edu.ui.screens.login.LoginPlaceholderScreen
import com.controlself.edu.ui.screens.welcome.WelcomeScreen

@Composable
fun ControlSelfNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.WELCOME
    ) {
        composable(Routes.WELCOME) {
            WelcomeScreen(
                onFinished = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.WELCOME) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.LOGIN) {
            LoginPlaceholderScreen()
        }
        composable(Routes.REGISTER) {
            LoginPlaceholderScreen()
        }
        composable(Routes.FORGOT_PASSWORD) {
            LoginPlaceholderScreen()
        }
        composable(Routes.STUDENT_HOME) {
            RoleHomeStubScreen(
                roleTitle = "Panel estudiante",
                upcomingPrp = "PRP-04"
            )
        }
        composable(Routes.TEACHER_HOME) {
            RoleHomeStubScreen(
                roleTitle = "Panel docente",
                upcomingPrp = "PRP-11"
            )
        }
        composable(Routes.PARENT_HOME) {
            RoleHomeStubScreen(
                roleTitle = "Panel padre / madre",
                upcomingPrp = "PRP-12"
            )
        }
    }
}
