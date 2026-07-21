package com.controlself.edu.domain.repository

import com.controlself.edu.domain.model.ChildAccount
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

    /**
     * Registro público: solo [UserRole.PARENT] y [UserRole.TEACHER].
     * [UserRole.STUDENT] no se auto-registra (usar [createChildAccount]).
     */
    suspend fun register(
        displayName: String,
        usernameOrEmail: String,
        password: String,
        role: UserRole
    ): Result<Session>

    /**
     * Alta de estudiante por un padre. No cambia la sesión actual.
     * Credenciales definidas por el padre.
     */
    suspend fun createChildAccount(
        parentUsername: String,
        displayName: String,
        usernameOrEmail: String,
        password: String
    ): Result<ChildAccount>

    /** Hijos vinculados a un padre (usernames en minúsculas). */
    suspend fun listChildren(parentUsername: String): List<ChildAccount>

    /** Mock: no envía email; solo valida que el usuario exista. */
    suspend fun requestPasswordReset(usernameOrEmail: String): Result<Unit>

    suspend fun logout()
}
