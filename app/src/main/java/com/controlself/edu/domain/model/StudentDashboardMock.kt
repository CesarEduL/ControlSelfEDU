package com.controlself.edu.domain.model

/**
 * Snapshot mock del dashboard estudiante (PRP-04).
 * Rachas/logros reales → PRP-09; stats → PRP-10.
 */
data class StudentDashboardMock(
    val streakDays: Int = 12,
    val badges: List<BadgeMock> = listOf(
        BadgeMock("Principiante", unlocked = true),
        BadgeMock("Constante", unlocked = true),
        BadgeMock("Estudiante destacado", unlocked = false),
        BadgeMock("Maestro del aprendizaje", unlocked = false)
    ),
    val averageScore: String = "16.5 / 20",
    val lessonsCompleted: Int = 4,
    val studyMinutes: Int = 85,
    val socialMinutes: Int = 18,
    val favoriteCourse: String = "Matemática"
)

data class BadgeMock(
    val title: String,
    val unlocked: Boolean
)
