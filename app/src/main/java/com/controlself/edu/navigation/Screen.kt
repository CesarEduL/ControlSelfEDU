package com.controlself.edu.navigation

import com.controlself.edu.domain.model.UserRole

object Routes {
    const val WELCOME = "welcome"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val FORGOT_PASSWORD = "forgot-password"

    const val STUDENT_HOME = "student/home"
    const val STUDENT_COURSE = "student/course/{courseId}"
    const val QUIZ_PLAY = "quiz/{courseId}/play"
    const val QUIZ_RESULT = "quiz/result/{attemptId}"
    const val TEACHER_HOME = "teacher/home"
    const val PARENT_HOME = "parent/home"

    const val LOCK = "lock"
    const val COURSE_SELECT = "course/select/{fromLock}"

    fun homeFor(role: UserRole): String = when (role) {
        UserRole.STUDENT -> STUDENT_HOME
        UserRole.TEACHER -> TEACHER_HOME
        UserRole.PARENT -> PARENT_HOME
    }

    fun studentCourse(courseId: String): String = "student/course/$courseId"

    fun quizPlay(courseId: String): String = "quiz/$courseId/play"

    fun quizResult(attemptId: String): String = "quiz/result/$attemptId"

    fun courseSelect(fromLock: Boolean): String = "course/select/$fromLock"
}
