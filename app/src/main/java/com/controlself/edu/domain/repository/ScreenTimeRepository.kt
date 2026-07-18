package com.controlself.edu.domain.repository

import com.controlself.edu.domain.model.parent.DayUsagePoint
import kotlinx.coroutines.flow.Flow

/**
 * Minutos de entretenimiento acumulados hoy (límite 30). Fuente: UsageStats (PRP-05).
 * Historial semanal para panel padre (PRP-12).
 */
interface ScreenTimeRepository {
    fun observeTodayMinutes(): Flow<Int>

    fun observeUsagePermissionGranted(): Flow<Boolean>

    /** Últimos 7 días (ISO) → minutos de entretenimiento registrados. */
    fun observeWeeklyEntertainmentMinutes(): Flow<Map<String, Int>>

    /** Reconsulta UsageStats y actualiza caché / lock si aplica. */
    suspend fun refresh()

    val dailyLimitMinutes: Int
        get() = DAILY_LIMIT_MINUTES

    companion object {
        const val DAILY_LIMIT_MINUTES = 30
    }
}
