package com.controlself.edu.domain.repository

import kotlinx.coroutines.flow.Flow

/**
 * Bloqueo de entretenimiento (PRP-06). Se levanta en PRP-08 al aprobar.
 */
interface LockRepository {
    fun observeLocked(): Flow<Boolean>

    suspend fun setLocked(locked: Boolean)
}
