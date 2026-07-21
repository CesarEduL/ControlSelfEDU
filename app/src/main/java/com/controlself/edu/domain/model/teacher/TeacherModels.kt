package com.controlself.edu.domain.model.teacher

import com.controlself.edu.domain.model.quiz.Question
import com.controlself.edu.domain.model.quiz.QuizAttempt

data class ClassroomStudent(
    val id: String,
    val displayName: String,
    val lastScores: List<Int> = emptyList(),
    val averageScore: Double? = null,
    val evaluationsCount: Int = 0,
    val isLocked: Boolean = false,
    /** true si los datos vienen del dispositivo (Estudiante Demo). */
    val isLocalDemo: Boolean = false
)

data class QuestionDifficulty(
    val questionId: String,
    val courseId: String,
    val prompt: String,
    val attempts: Int,
    val wrongCount: Int
) {
    val accuracyPercent: Int
        get() = if (attempts <= 0) 100 else ((attempts - wrongCount) * 100 / attempts)

    val errorPercent: Int
        get() = 100 - accuracyPercent
}

data class TeacherDashboard(
    val studentCount: Int = 0,
    val classroomAverage: Double? = null,
    val publishedCourses: Int = 0,
    val hardTopicsPreview: List<QuestionDifficulty> = emptyList(),
    val students: List<ClassroomStudent> = emptyList()
)

data class CourseBankStatus(
    val courseId: String,
    val courseTitle: String,
    val questionCount: Int,
    val isReady: Boolean
) {
    companion object {
        val REQUIRED: Int get() = QuizAttempt.TOTAL_QUESTIONS
    }
}

data class EditableQuestion(
    val question: Question,
    val courseId: String
)
