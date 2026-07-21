package com.controlself.edu.domain.repository

import kotlinx.coroutines.flow.Flow

/**
 * Bloqueo de entretenimiento (PRP-06 / PRP-08).
 *
 * Política de desbloqueo (PRP-08): al aprobar (≥ PASS_THRESHOLD) se concede un **pase el resto del día**.
 * UsageStats no vuelve a bloquear hasta el día siguiente, aunque el contador siga ≥ 30 min.
 */
interface LockRepository {
    fun observeLocked(): Flow<Boolean>

    suspend fun setLocked(locked: Boolean)

    /** Desbloquea y marca pase válido hasta el cambio de día local. */
    suspend fun unlockForRestOfDay()

    /** True si hoy ya se aprobó una lección (no re-bloquear por tiempo). */
    suspend fun hasRestOfDayPass(): Boolean
}
