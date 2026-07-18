package com.controlself.edu.domain.repository

import com.controlself.edu.domain.model.Session
import com.controlself.edu.domain.model.UserRole
import kotlinx.coroutines.flow.Flow

/**
 * Contrato de autenticación. Implementación mock en `data` (PRP-03 completará UI + remember).
 */
interface AuthRepository {
    val session: Flow<Session?>

    suspend fun login(
        usernameOrEmail: String,
        password: String,
        role: UserRole,
        remember: Boolean
    ): Result<Session>

    suspend fun logout()
}
