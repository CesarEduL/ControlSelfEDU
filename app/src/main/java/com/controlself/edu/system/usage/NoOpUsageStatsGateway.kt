package com.controlself.edu.system.usage

/**
 * Stub: sin permiso ni lecturas reales (PRP-05).
 */
class NoOpUsageStatsGateway : UsageStatsGateway {
    override fun hasUsageAccessPermission(): Boolean = false

    override fun queryEntertainmentMinutesToday(): Int? = null
}
