package com.controlself.edu.navigation

import com.controlself.edu.domain.model.UserRole

object Routes {
    const val WELCOME = "welcome"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val FORGOT_PASSWORD = "forgot-password"

    const val STUDENT_HOME = "student/home"
    const val STUDENT_COURSE = "student/course/{courseId}"
    const val TEACHER_HOME = "teacher/home"
    const val PARENT_HOME = "parent/home"

    fun homeFor(role: UserRole): String = when (role) {
        UserRole.STUDENT -> STUDENT_HOME
        UserRole.TEACHER -> TEACHER_HOME
        UserRole.PARENT -> PARENT_HOME
    }

    fun studentCourse(courseId: String): String = "student/course/$courseId"
}

sealed class Screen(val route: String) {
    data object Welcome : Screen(Routes.WELCOME)
    data object Login : Screen(Routes.LOGIN)
    data object Register : Screen(Routes.REGISTER)
    data object ForgotPassword : Screen(Routes.FORGOT_PASSWORD)
    data object StudentHome : Screen(Routes.STUDENT_HOME)
    data object StudentCourse : Screen(Routes.STUDENT_COURSE)
    data object TeacherHome : Screen(Routes.TEACHER_HOME)
    data object ParentHome : Screen(Routes.PARENT_HOME)
}
