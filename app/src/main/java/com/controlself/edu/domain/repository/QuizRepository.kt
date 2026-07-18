package com.controlself.edu.domain.repository

import com.controlself.edu.domain.model.quiz.Question
import com.controlself.edu.domain.model.quiz.QuizAttempt
import kotlinx.coroutines.flow.Flow

interface QuizRepository {
    fun questionsFor(courseId: String): List<Question>
}

interface QuizAttemptRepository {
    val latestAttempt: Flow<QuizAttempt?>

    suspend fun save(attempt: QuizAttempt)

    fun getById(id: String): QuizAttempt?
}
