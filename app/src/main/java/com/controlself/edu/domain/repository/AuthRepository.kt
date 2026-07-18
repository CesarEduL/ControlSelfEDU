package com.controlself.edu.domain.repository

import com.controlself.edu.domain.model.Session
import com.controlself.edu.domain.model.UserRole
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    /** Sesión actual; null si no hay login. */
    val session: Flow<Session?>

    /** true cuando DataStore ya restauró (o descartó) la sesión recordada. */
    val isRestored: Flow<Boolean>

    suspend fun login(
        usernameOrEmail: String,
        password: String,
        role: UserRole,
        remember: Boolean
    ): Result<Session>

    suspend fun register(
        displayName: String,
        usernameOrEmail: String,
        password: String,
        role: UserRole
    ): Result<Session>

    /** Mock: no envía email; solo valida que el usuario exista. */
    suspend fun requestPasswordReset(usernameOrEmail: String): Result<Unit>

    suspend fun logout()
}
