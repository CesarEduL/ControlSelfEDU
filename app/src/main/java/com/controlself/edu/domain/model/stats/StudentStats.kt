package com.controlself.edu.domain.model.stats

/**
 * Resumen de progreso del estudiante (PRP-10).
 * Agregado desde intentos, screen time y motivación — no es una segunda verdad.
 */
data class AttemptStat(
    val id: String,
    val courseId: String,
    val courseTitle: String,
    val correctCount: Int,
    val total: Int,
    val passed: Boolean,
    val timestampMillis: Long,
    val durationMillis: Long
) {
    val durationMinutes: Int
        get() = ((durationMillis + 59_999L) / 60_000L).toInt().coerceAtLeast(0)
}

data class CourseStatBar(
    val courseId: String,
    val label: String,
    val passedCount: Int,
    val attemptCount: Int
)

data class ScorePoint(
    val label: String,
    val score: Int,
    val maxScore: Int = 20
)

data class StudentStatsDashboard(
    val evaluationsCount: Int = 0,
    val averageScore: Double? = null,
    val studyMinutesTotal: Int = 0,
    val entertainmentMinutesToday: Int = 0,
    val coursesPassedDistinct: Int = 0,
    val achievementsUnlocked: Int = 0,
    val achievementsTotal: Int = 0,
    val currentStreak: Int = 0,
    val maxStreak: Int = 0,
    /** Últimos intentos (más reciente al final) para gráfico de notas. */
    val recentScores: List<ScorePoint> = emptyList(),
    val courseBars: List<CourseStatBar> = emptyList(),
    val hasAttemptData: Boolean = false
)
