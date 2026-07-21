package com.controlself.edu.domain.repository

import com.controlself.edu.domain.model.motivation.MotivationSnapshot
import com.controlself.edu.domain.model.quiz.QuizAttempt
import kotlinx.coroutines.flow.Flow

/**
 * Rachas y logros (PRP-09).
 *
 * Regla de racha: un día calendario cuenta si hubo al menos un intento de evaluación
 * completado ese día. Se rompe si pasa un día sin actividad.
 */
interface AchievementRepository {

    fun observeSnapshot(): Flow<MotivationSnapshot>

    /** Tras guardar un intento: actualiza racha, stats y evalúa logros. */
    suspend fun onQuizAttempt(attempt: QuizAttempt)

    /**
     * Sync diario de “uso responsable”: minutos de entretenimiento y si hubo bloqueo forzado.
     * Llamar desde el home al reanudar / refrescar.
     */
    suspend fun syncResponsibleUsage(minutesUsed: Int, isForceLocked: Boolean)

    companion object {
        const val RESPONSIBLE_DAYS_REQUIRED = 3
        const val OUTSTANDING_STREAK = 7
        /** ~80% del total de preguntas (antes 16/20). */
        val OUTSTANDING_AVERAGE: Double get() = QuizAttempt.TOTAL_QUESTIONS * 0.8
        const val MASTER_STREAK = 30
        const val MASTER_PASSED = 20
        const val CONSISTENT_STREAK = 3
    }
}
