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
            AnswerRecord("2", "Un triángulo tiene 3 lados.", "Verdadero", "Verdadero", true),
            AnswerRecord("3", "Si un producto cuesta 50 y tiene 10% de descuento, ¿cuánto pagas?", "45", "45", true),
            AnswerRecord("4", "El número 17 es par.", "Falso", "Falso", true)
        ),
        correctCount = QuizAttempt.PASS_THRESHOLD,
        total = QuizAttempt.TOTAL_QUESTIONS
    )

    val quizAttemptFailed = QuizAttempt(
        id = "preview-fail",
        courseId = "math",
        courseTitle = "Matemática",
        answers = listOf(
            AnswerRecord("1", "¿Cuánto es 7 × 8?", "54", "56", false),
            AnswerRecord("2", "Un triángulo tiene 3 lados.", "Falso", "Verdadero", false),
            AnswerRecord("3", "Si un producto cuesta 50 y tiene 10% de descuento, ¿cuánto pagas?", "45", "45", true),
            AnswerRecord("4", "El número 17 es par.", "Verdadero", "Falso", false)
        ),
        correctCount = 1,
        total = QuizAttempt.TOTAL_QUESTIONS
    )
}
