package com.controlself.edu.system.usage

import android.content.Context
import android.content.Intent
import android.provider.Settings

/** Stub / preview sin UsageStats. */
class NoOpUsageStatsGateway(
    private val context: Context? = null
) : UsageStatsGateway {
    override fun hasUsageAccessPermission(): Boolean = false

    override fun queryEntertainmentMinutesToday(): Int? = null

    override fun openUsageAccessSettings() {
        context?.let {
            val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            it.startActivity(intent)
        }
    }
}
