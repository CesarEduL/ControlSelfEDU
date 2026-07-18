package com.controlself.edu.ui.preview

import com.controlself.edu.domain.model.quiz.AnswerRecord
import com.controlself.edu.domain.model.quiz.QuizAttempt

object PreviewSamples {
    val quizAttemptPassed = QuizAttempt(
        id = "preview-pass",
        courseId = "math",
        courseTitle = "Matemática",
        answers = listOf(
            AnswerRecord("1", "¿Cuánto es 7 × 8?", "56", "56", true),
            AnswerRecord("2", "¿Cuál es la mitad de 48?", "24", "24", true),
            AnswerRecord("3", "Un triángulo tiene 3 lados.", "Verdadero", "Verdadero", true)
        ),
        correctCount = 16,
        total = QuizAttempt.TOTAL_QUESTIONS
    )

    val quizAttemptFailed = QuizAttempt(
        id = "preview-fail",
        courseId = "math",
        courseTitle = "Matemática",
        answers = listOf(
            AnswerRecord("1", "¿Cuánto es 7 × 8?", "54", "56", false),
            AnswerRecord("2", "¿Cuál es la mitad de 48?", "24", "24", true),
            AnswerRecord("3", "Un triángulo tiene 3 lados.", "Falso", "Verdadero", false)
        ),
        correctCount = 10,
        total = QuizAttempt.TOTAL_QUESTIONS
    )
}
