package com.controlself.edu.domain.repository

import com.controlself.edu.domain.model.quiz.QuizAttempt
import com.controlself.edu.domain.model.stats.AttemptStat
import kotlinx.coroutines.flow.Flow

/**
 * Historial ligero de intentos para gráficos (PRP-10).
 * No guarda respuestas — solo métricas agregables.
 */
interface StatsRepository {
    fun observeAttempts(): Flow<List<AttemptStat>>

    suspend fun recordAttempt(attempt: QuizAttempt)
}
