package com.controlself.edu.domain.model

/**
 * Sesión local (mock-first). Persistencia real en PRP-03 (DataStore).
 */
data class Session(
    val userId: String,
    val displayName: String,
    val role: UserRole
)
