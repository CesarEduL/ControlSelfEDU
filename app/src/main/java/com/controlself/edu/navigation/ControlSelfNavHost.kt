package com.controlself.edu.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.controlself.edu.domain.model.UserRole
import com.controlself.edu.ui.screens.forgot.ForgotPasswordScreen
import com.controlself.edu.ui.screens.lock.CourseSelectScreen
import com.controlself.edu.ui.screens.lock.LockScreen
import com.controlself.edu.ui.screens.login.LoginScreen
import com.controlself.edu.ui.screens.parent.ParentAttemptDetailScreen
import com.controlself.edu.ui.screens.parent.ParentHomeScreen
import com.controlself.edu.ui.screens.protection.AdminProtectionScreen
import com.controlself.edu.ui.screens.quiz.CourseIntroScreen
import com.controlself.edu.ui.screens.quiz.QuizPlayScreen
import com.controlself.edu.ui.screens.quiz.QuizResultScreen
import com.controlself.edu.ui.screens.quiz.QuizReviewScreen
import com.controlself.edu.ui.screens.register.RegisterScreen
import com.controlself.edu.ui.screens.student.StudentHomeScreen
import com.controlself.edu.ui.screens.teacher.TeacherBankScreen
import com.controlself.edu.ui.screens.teacher.TeacherHomeScreen
import com.controlself.edu.ui.screens.teacher.TeacherQuestionEditScreen
import com.controlself.edu.ui.screens.teacher.TeacherQuestionListScreen
import com.controlself.edu.ui.screens.teacher.TeacherReportsScreen
import com.controlself.edu.ui.screens.teacher.TeacherStatsScreen
import com.controlself.edu.ui.screens.teacher.TeacherStudentDetailScreen
import com.controlself.edu.ui.screens.teacher.TeacherStudentsScreen
import com.controlself.edu.ui.screens.welcome.WelcomeScreen
import com.controlself.edu.ui.theme.CseBlue
import kotlinx.coroutines.launch

@Composable
fun ControlSelfNavHost() {
    val container = LocalAppContainer.current
    val auth = container.authRepository
    val lockRepository = container.lockRepository
    val restored by auth.isRestored.collectAsStateWithLifecycle(initialValue = false)
    val session by auth.session.collectAsStateWithLifecycle(initialValue = null)
    val locked by lockRepository.observeLocked().collectAsStateWithLifecycle(initialValue = false)
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()

    if (!restored) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = CseBlue)
        }
        return
    }

    val startDestination = when {
        session == null -> Routes.WELCOME
        session!!.role == UserRole.STUDENT && locked -> Routes.LOCK
        else -> Routes.homeFor(session!!.role)
    }

    fun goHome(session: Session) {
        val dest = if (session.role == UserRole.STUDENT && locked) {
            Routes.LOCK
        } else {
            Routes.homeFor(session.role)
        }
        navController.navigate(dest) {
            popUpTo(navController.graph.id) { inclusive = true }
        }
    }

    fun goStudentHomeClearing() {
        navController.navigate(Routes.STUDENT_HOME) {
            popUpTo(navController.graph.id) { inclusive = true }
        }
    }

    fun goLoginClearingBackStack() {
        navController.navigate(Routes.LOGIN) {
            popUpTo(navController.graph.id) { inclusive = true }
        }
    }

    LaunchedEffect(locked, session?.role) {
        val isStudent = session?.role == UserRole.STUDENT
        val route = navController.currentBackStackEntry?.destination?.route.orEmpty()
        val onLockFlow = route == Routes.LOCK ||
            route.startsWith("course/select") ||
            route.startsWith("student/course") ||
            route.startsWith("quiz/")
        if (isStudent && locked && !onLockFlow) {
            navController.navigate(Routes.LOCK) {
                launchSingleTop = true
            }
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
        composable(Routes.LOCK) {
            LockScreen(
                onStartLesson = {
                    navController.navigate(Routes.courseSelect(fromLock = true))
                }
            )
        }
        composable(
            route = Routes.COURSE_SELECT,
            arguments = listOf(navArgument("fromLock") { type = NavType.BoolType })
        ) { entry ->
            val fromLock = entry.arguments?.getBoolean("fromLock") == true
            CourseSelectScreen(
                fromLock = fromLock,
                onCourseClick = { course ->
                    navController.navigate(Routes.studentCourse(course.id))
                },
                onBack = {
                    if (fromLock) {
                        navController.popBackStack(Routes.LOCK, inclusive = false)
                    } else {
                        navController.popBackStack()
                    }
                }
            )
        }
        composable(Routes.STUDENT_HOME) {
            StudentHomeScreen(
                displayName = session?.displayName ?: "Estudiante",
                onCourseClick = { course ->
                    navController.navigate(Routes.studentCourse(course.id))
                },
                onSimulateLock = {
                    scope.launch { lockRepository.setLocked(true) }
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
            CourseIntroScreen(
                course = course,
                onStartQuiz = { navController.navigate(Routes.quizPlay(course.id)) },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = Routes.QUIZ_PLAY,
            arguments = listOf(navArgument("courseId") { type = NavType.StringType })
        ) { entry ->
            val courseId = entry.arguments?.getString("courseId").orEmpty()
            val course = Course.fromId(courseId) ?: Course.MATH
            QuizPlayScreen(
                course = course,
                onFinished = { attemptId ->
                    navController.navigate(Routes.quizResult(attemptId)) {
                        popUpTo(Routes.quizPlay(course.id)) { inclusive = true }
                    }
                },
                onAbort = { navController.popBackStack() }
            )
        }
        composable(
            route = Routes.QUIZ_RESULT,
            arguments = listOf(navArgument("attemptId") { type = NavType.StringType })
        ) { entry ->
            val attemptId = entry.arguments?.getString("attemptId").orEmpty()
            QuizResultScreen(
                attemptId = attemptId,
                onReview = { id ->
                    navController.navigate(Routes.quizReview(id))
                },
                onRetry = { courseId ->
                    navController.navigate(Routes.quizPlay(courseId)) {
                        popUpTo(Routes.QUIZ_RESULT) { inclusive = true }
                    }
                },
                onHome = { goStudentHomeClearing() },
                onBackToLock = {
                    navController.navigate(Routes.LOCK) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                }
            )
        }
        composable(
            route = Routes.QUIZ_REVIEW,
            arguments = listOf(navArgument("attemptId") { type = NavType.StringType })
        ) { entry ->
            val attemptId = entry.arguments?.getString("attemptId").orEmpty()
            QuizReviewScreen(
                attemptId = attemptId,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.TEACHER_HOME) {
            TeacherHomeScreen(
                displayName = session?.displayName.orEmpty(),
                onOpenBank = { navController.navigate(Routes.TEACHER_BANK) },
                onOpenStudents = { navController.navigate(Routes.TEACHER_STUDENTS) },
                onOpenStats = { navController.navigate(Routes.TEACHER_STATS) },
                onOpenReports = { navController.navigate(Routes.TEACHER_REPORTS) },
                onOpenProtection = { navController.navigate(Routes.ADMIN_PROTECTION) },
                onLogout = {
                    scope.launch {
                        auth.logout()
                        goLoginClearingBackStack()
                    }
                }
            )
        }
        composable(Routes.TEACHER_BANK) {
            TeacherBankScreen(
                onBack = { navController.popBackStack() },
                onOpenCourse = { courseId ->
                    navController.navigate(Routes.teacherBankCourse(courseId))
                }
            )
        }
        composable(
            route = Routes.TEACHER_BANK_COURSE,
            arguments = listOf(navArgument("courseId") { type = NavType.StringType })
        ) { entry ->
            val courseId = entry.arguments?.getString("courseId").orEmpty()
            TeacherQuestionListScreen(
                courseId = courseId,
                onBack = { navController.popBackStack() },
                onEdit = { questionId ->
                    navController.navigate(Routes.teacherQuestionEdit(courseId, questionId))
                },
                onAdd = {
                    navController.navigate(Routes.teacherQuestionEdit(courseId, "new"))
                }
            )
        }
        composable(
            route = Routes.TEACHER_QUESTION_EDIT,
            arguments = listOf(
                navArgument("courseId") { type = NavType.StringType },
                navArgument("questionId") { type = NavType.StringType }
            )
        ) { entry ->
            val courseId = entry.arguments?.getString("courseId").orEmpty()
            val questionId = entry.arguments?.getString("questionId").orEmpty()
            TeacherQuestionEditScreen(
                courseId = courseId,
                questionId = questionId.takeIf { it != "new" },
                onDone = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.TEACHER_STUDENTS) {
            TeacherStudentsScreen(
                onBack = { navController.popBackStack() },
                onOpenStudent = { id ->
                    navController.navigate(Routes.teacherStudent(id))
                }
            )
        }
        composable(
            route = Routes.TEACHER_STUDENT,
            arguments = listOf(navArgument("studentId") { type = NavType.StringType })
        ) { entry ->
            val studentId = entry.arguments?.getString("studentId").orEmpty()
            TeacherStudentDetailScreen(
                studentId = studentId,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.TEACHER_STATS) {
            TeacherStatsScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.TEACHER_REPORTS) {
            TeacherReportsScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.PARENT_HOME) {
            ParentHomeScreen(
                displayName = session?.displayName.orEmpty(),
                onOpenAttempt = { attemptId ->
                    navController.navigate(Routes.parentAttempt(attemptId))
                },
                onOpenProtection = { navController.navigate(Routes.ADMIN_PROTECTION) },
                onLogout = {
                    scope.launch {
                        auth.logout()
                        goLoginClearingBackStack()
                    }
                }
            )
        }
        composable(
            route = Routes.PARENT_ATTEMPT,
            arguments = listOf(navArgument("attemptId") { type = NavType.StringType })
        ) { entry ->
            val attemptId = entry.arguments?.getString("attemptId").orEmpty()
            ParentAttemptDetailScreen(
                attemptId = attemptId,
                onBack = { navController.popBackStack() },
                onOpenAnswers = { id ->
                    navController.navigate(Routes.quizReview(id))
                }
            )
        }
        composable(Routes.ADMIN_PROTECTION) {
            AdminProtectionScreen(onBack = { navController.popBackStack() })
        }
    }
}
