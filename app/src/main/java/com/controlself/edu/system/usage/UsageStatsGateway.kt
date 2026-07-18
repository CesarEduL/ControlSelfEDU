package com.controlself.edu.system.usage

/**
 * Abstracción de UsageStats. Implementación real en PRP-05.
 * La UI no debe depender de APIs del sistema directamente.
 */
interface UsageStatsGateway {
    /** true si el usuario concedió Usage Access. */
    fun hasUsageAccessPermission(): Boolean

    /**
     * Minutos en foreground hoy para el catálogo de apps de entretenimiento.
     * @return null si no hay permiso o no se pudo leer.
     */
    fun queryEntertainmentMinutesToday(): Int?
}
