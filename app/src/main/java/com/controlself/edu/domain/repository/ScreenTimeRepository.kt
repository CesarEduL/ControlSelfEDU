package com.controlself.edu.domain.repository

import kotlinx.coroutines.flow.Flow

/**
 * Minutos de entretenimiento acumulados hoy (límite 30). Fuente: UsageStats (PRP-05).
 */
interface ScreenTimeRepository {
    fun observeTodayMinutes(): Flow<Int>

    fun observeUsagePermissionGranted(): Flow<Boolean>

    /** Reconsulta UsageStats y actualiza caché / lock si aplica. */
    suspend fun refresh()

    val dailyLimitMinutes: Int
        get() = DAILY_LIMIT_MINUTES

    companion object {
        const val DAILY_LIMIT_MINUTES = 30
    }
}
