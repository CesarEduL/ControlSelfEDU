package com.controlself.edu.domain.model.quiz

enum class QuestionType {
    MULTIPLE_CHOICE,
    TRUE_FALSE
}

data class Question(
    val id: String,
    val prompt: String,
    val type: QuestionType,
    /** Opciones en orden; para V/F: ["Verdadero", "Falso"]. */
    val options: List<String>,
    /** Índice de la opción correcta (0-based). */
    val correctIndex: Int
)

data class AnswerRecord(
    val questionId: String,
    val prompt: String,
    val userAnswerLabel: String,
    val correctAnswerLabel: String,
    val isCorrect: Boolean
)

data class QuizAttempt(
    val id: String,
    val courseId: String,
    val courseTitle: String,
    val answers: List<AnswerRecord>,
    val correctCount: Int,
    val total: Int = TOTAL_QUESTIONS,
    val timestampMillis: Long = System.currentTimeMillis(),
    /** Duración de la sesión de quiz (PRP-10). */
    val durationMillis: Long = 0L
) {
    val passed: Boolean get() = correctCount >= PASS_THRESHOLD

    val durationMinutes: Int
        get() = ((durationMillis + 59_999L) / 60_000L).toInt().coerceAtLeast(0)

    companion object {
        const val TOTAL_QUESTIONS = 20
        const val PASS_THRESHOLD = 15
    }
}
