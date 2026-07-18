package com.controlself.edu.domain.repository

import kotlinx.coroutines.flow.Flow

/**
 * Estado de bloqueo de entretenimiento (PRP-06). Stub hasta monitoreo real.
 */
interface LockRepository {
    fun observeLocked(): Flow<Boolean>

    suspend fun setLocked(locked: Boolean)
}
