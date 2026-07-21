package com.controlself.edu.domain.model

/** Cuenta de estudiante creada por un padre (jerarquía 1→N). */
data class ChildAccount(
    val userId: String,
    val displayName: String
)
