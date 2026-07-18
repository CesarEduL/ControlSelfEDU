package com.controlself.edu.data.quiz

import com.controlself.edu.domain.model.quiz.QuizAttempt
import com.controlself.edu.domain.repository.QuizAttemptRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class InMemoryQuizAttemptRepository : QuizAttemptRepository {

    private val attempts = LinkedHashMap<String, QuizAttempt>()
    private val _latest = MutableStateFlow<QuizAttempt?>(null)

    override val latestAttempt: Flow<QuizAttempt?> = _latest.asStateFlow()

    override suspend fun save(attempt: QuizAttempt) {
        attempts[attempt.id] = attempt
        _latest.value = attempt
    }

    override fun getById(id: String): QuizAttempt? = attempts[id]
}
