package com.controlself.edu.domain.model.motivation

/**
 * Catálogo fijo de logros MVP (PRP-09). Cada id se desbloquea como máximo una vez.
 */
enum class AchievementId(
    val title: String,
    val description: String
) {
    BEGINNER(
        title = "Principiante",
        description = "Primera evaluación completada"
    ),
    CONSISTENT(
        title = "Constante",
        description = "Racha de 3 días o más"
    ),
    OUTSTANDING(
        title = "Estudiante destacado",
        description = "Racha ≥ 7 o promedio ≥ 80%"
    ),
    MASTER(
        title = "Maestro del aprendizaje",
        description = "Racha ≥ 30 o 20 evaluaciones aprobadas"
    ),
    RESPONSIBLE(
        title = "Uso responsable",
        description = "3 días bajo el límite sin bloqueo forzado"
    );

    companion object {
        val ALL: List<AchievementId> = entries
    }
}

data class StreakState(
    val currentDays: Int = 0,
    /** Día ISO (yyyy-MM-dd) de la última actividad que contó para la racha. */
    val lastActivityDay: String? = null
)

data class UnlockedAchievement(
    val id: AchievementId,
    val unlockedAtMillis: Long
)

/**
 * Vista lista para UI: racha + insignias (locked/unlocked) + historial.
 */
data class MotivationSnapshot(
    val streak: StreakState = StreakState(),
    /** Mejor racha histórica (PRP-10). */
    val maxStreakDays: Int = 0,
    val unlocked: List<UnlockedAchievement> = emptyList(),
    val completedAttempts: Int = 0,
    val passedAttempts: Int = 0,
    val averageScore: Double? = null
) {
    fun isUnlocked(id: AchievementId): Boolean =
        unlocked.any { it.id == id }

    fun unlockedAt(id: AchievementId): Long? =
        unlocked.find { it.id == id }?.unlockedAtMillis

    val badges: List<AchievementBadge>
        get() = AchievementId.ALL.map { id ->
            AchievementBadge(
                id = id,
                title = id.title,
                description = id.description,
                unlocked = isUnlocked(id),
                unlockedAtMillis = unlockedAt(id)
            )
        }
}

data class AchievementBadge(
    val id: AchievementId,
    val title: String,
    val description: String,
    val unlocked: Boolean,
    val unlockedAtMillis: Long? = null
)
