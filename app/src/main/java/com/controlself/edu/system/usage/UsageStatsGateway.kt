package com.controlself.edu.system.usage

/**
 * Abstracción de UsageStats. La UI no llama APIs del sistema directamente.
 */
interface UsageStatsGateway {
    fun hasUsageAccessPermission(): Boolean

    /**
     * Minutos en foreground hoy para apps de entretenimiento.
     * null si no hay permiso.
     */
    fun queryEntertainmentMinutesToday(): Int?

    fun openUsageAccessSettings()
}
