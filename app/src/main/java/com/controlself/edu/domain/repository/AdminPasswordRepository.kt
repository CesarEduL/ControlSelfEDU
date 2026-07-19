package com.controlself.edu.domain.repository

import kotlinx.coroutines.flow.Flow

/**
 * Contraseña de administrador anti-desinstalación (PRP-13).
 * Distinta de la contraseña de login.
 */
interface AdminPasswordRepository {
    fun observeIsSet(): Flow<Boolean>

    suspend fun isSet(): Boolean

    /** Define o cambia la contraseña. Si ya hay una, [currentPassword] debe ser correcta. */
    suspend fun setPassword(newPassword: String, currentPassword: String?): Result<Unit>

    suspend fun verify(password: String): Boolean
}
