package com.controlself.edu.domain.repository

import com.controlself.edu.domain.model.quiz.Question
import com.controlself.edu.domain.model.quiz.QuizAttempt
import com.controlself.edu.domain.model.teacher.CourseBankStatus
import kotlinx.coroutines.flow.Flow

interface QuizRepository {
    fun questionsFor(courseId: String): List<Question>

    fun observeQuestions(courseId: String): Flow<List<Question>>

    fun observeBankStatus(): Flow<List<CourseBankStatus>>

    suspend fun upsertQuestion(courseId: String, question: Question)

    suspend fun deleteQuestion(courseId: String, questionId: String)

    suspend fun replaceAll(courseId: String, questions: List<Question>)
}

interface QuizAttemptRepository {
    val latestAttempt: Flow<QuizAttempt?>

    suspend fun save(attempt: QuizAttempt)

    fun getById(id: String): QuizAttempt?
}
