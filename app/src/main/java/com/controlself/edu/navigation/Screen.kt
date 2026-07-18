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
    const val QUIZ_REVIEW = "quiz/review/{attemptId}"
    const val TEACHER_HOME = "teacher/home"
    const val TEACHER_BANK = "teacher/bank"
    const val TEACHER_BANK_COURSE = "teacher/bank/{courseId}"
    const val TEACHER_QUESTION_EDIT = "teacher/bank/{courseId}/edit/{questionId}"
    const val TEACHER_STUDENTS = "teacher/students"
    const val TEACHER_STUDENT = "teacher/students/{studentId}"
    const val TEACHER_STATS = "teacher/stats"
    const val TEACHER_REPORTS = "teacher/reports"
    const val PARENT_HOME = "parent/home"
    const val PARENT_ATTEMPT = "parent/attempt/{attemptId}"

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

    fun quizReview(attemptId: String): String = "quiz/review/$attemptId"

    fun courseSelect(fromLock: Boolean): String = "course/select/$fromLock"

    fun teacherBankCourse(courseId: String): String = "teacher/bank/$courseId"

    fun teacherQuestionEdit(courseId: String, questionId: String): String =
        "teacher/bank/$courseId/edit/$questionId"

    fun teacherStudent(studentId: String): String = "teacher/students/$studentId"

    fun parentAttempt(attemptId: String): String = "parent/attempt/$attemptId"
}
