package com.controlself.edu.domain.repository

import kotlinx.coroutines.flow.Flow

/**
 * Minutos de entretenimiento del día. Fuente real: UsageStats (PRP-05).
 */
interface ScreenTimeRepository {
    /** Minutos acumulados hoy (límite de producto: 30). */
    fun observeTodayMinutes(): Flow<Int>

    val dailyLimitMinutes: Int
        get() = DAILY_LIMIT_MINUTES

    companion object {
        const val DAILY_LIMIT_MINUTES = 30
    }
}
