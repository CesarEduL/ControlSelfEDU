package com.controlself.edu.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.controlself.edu.di.LocalAppContainer
import com.controlself.edu.domain.model.Course
import com.controlself.edu.domain.model.Session
import com.controlself.edu.ui.screens.common.RoleHomeStubScreen
import com.controlself.edu.ui.screens.forgot.ForgotPasswordScreen
import com.controlself.edu.ui.screens.login.LoginScreen
import com.controlself.edu.ui.screens.register.RegisterScreen
import com.controlself.edu.ui.screens.student.CoursePlaceholderScreen
import com.controlself.edu.ui.screens.student.StudentHomeScreen
import com.controlself.edu.ui.screens.welcome.WelcomeScreen
import com.controlself.edu.ui.theme.CseBlue
import kotlinx.coroutines.launch

@Composable
fun ControlSelfNavHost() {
    val container = LocalAppContainer.current
    val auth = container.authRepository
    val restored by auth.isRestored.collectAsStateWithLifecycle(initialValue = false)
    val session by auth.session.collectAsStateWithLifecycle(initialValue = null)
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()

    if (!restored) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = CseBlue)
        }
        return
    }

    val startDestination = if (session != null) {
        Routes.homeFor(session!!.role)
    } else {
        Routes.WELCOME
    }

    fun goHome(session: Session) {
        navController.navigate(Routes.homeFor(session.role)) {
            popUpTo(navController.graph.id) { inclusive = true }
        }
    }

    fun goLoginClearingBackStack() {
        navController.navigate(Routes.LOGIN) {
            popUpTo(navController.graph.id) { inclusive = true }
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
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
            LoginScreen(
                onLoggedIn = ::goHome,
                onCreateAccount = { navController.navigate(Routes.REGISTER) },
                onForgotPassword = { navController.navigate(Routes.FORGOT_PASSWORD) }
            )
        }
        composable(Routes.REGISTER) {
            RegisterScreen(
                onRegistered = ::goHome,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.FORGOT_PASSWORD) {
            ForgotPasswordScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.STUDENT_HOME) {
            StudentHomeScreen(
                displayName = session?.displayName ?: "Estudiante",
                onCourseClick = { course ->
                    navController.navigate(Routes.studentCourse(course.id))
                },
                onLogout = {
                    scope.launch {
                        auth.logout()
                        goLoginClearingBackStack()
                    }
                }
            )
        }
        composable(
            route = Routes.STUDENT_COURSE,
            arguments = listOf(navArgument("courseId") { type = NavType.StringType })
        ) { entry ->
            val courseId = entry.arguments?.getString("courseId").orEmpty()
            val course = Course.fromId(courseId) ?: Course.MATH
            CoursePlaceholderScreen(
                course = course,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.TEACHER_HOME) {
            RoleHomeStubScreen(
                roleTitle = "Panel docente",
                upcomingPrp = "PRP-11",
                displayName = session?.displayName,
                onLogout = {
                    scope.launch {
                        auth.logout()
                        goLoginClearingBackStack()
                    }
                }
            )
        }
        composable(Routes.PARENT_HOME) {
            RoleHomeStubScreen(
                roleTitle = "Panel padre / madre",
                upcomingPrp = "PRP-12",
                displayName = session?.displayName,
                onLogout = {
                    scope.launch {
                        auth.logout()
                        goLoginClearingBackStack()
                    }
                }
            )
        }
    }
}
