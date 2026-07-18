package com.controlself.edu.data.auth

import com.controlself.edu.domain.model.Session
import com.controlself.edu.domain.model.UserRole
import com.controlself.edu.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Auth en memoria. Sustituible por DataStore/Room en PRP-03.
 */
class InMemoryAuthRepository : AuthRepository {

    private val _session = MutableStateFlow<Session?>(null)
    override val session: Flow<Session?> = _session.asStateFlow()

    override suspend fun login(
        usernameOrEmail: String,
        password: String,
        role: UserRole,
        remember: Boolean
    ): Result<Session> {
        if (usernameOrEmail.isBlank() || password.isBlank()) {
            return Result.failure(IllegalArgumentException("Usuario y contraseña requeridos"))
        }
        val session = Session(
            userId = "local-${role.name.lowercase()}",
            displayName = usernameOrEmail.trim(),
            role = role
        )
        _session.value = session
        return Result.success(session)
    }

    override suspend fun logout() {
        _session.value = null
    }
}
