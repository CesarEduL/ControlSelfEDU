package com.controlself.edu.domain.model

enum class UserRole {
    STUDENT,
    TEACHER,
    PARENT
}

fun UserRole.labelEs(): String = when (this) {
    UserRole.STUDENT -> "Estudiante"
    UserRole.TEACHER -> "Docente"
    UserRole.PARENT -> "Padre de familia"
}
