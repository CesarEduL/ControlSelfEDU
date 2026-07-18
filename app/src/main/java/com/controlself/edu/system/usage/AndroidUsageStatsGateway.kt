package com.controlself.edu.system.usage

import android.app.AppOpsManager
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.os.Process
import android.provider.Settings
import java.util.Calendar

class AndroidUsageStatsGateway(
    private val context: Context
) : UsageStatsGateway {

    private val appContext = context.applicationContext

    override fun hasUsageAccessPermission(): Boolean {
        val appOps = appContext.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            Process.myUid(),
            appContext.packageName
        )
        return mode == AppOpsManager.MODE_ALLOWED
    }

    override fun queryEntertainmentMinutesToday(): Int? {
        if (!hasUsageAccessPermission()) return null

        val usageStatsManager =
            appContext.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val start = startOfTodayMillis()
        val end = System.currentTimeMillis()
        val stats = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_BEST,
            start,
            end
        ) ?: return 0

        val gamePackages = resolveGamePackages()
        val monitored = EntertainmentAppCatalog.socialPackages + gamePackages

        var totalMs = 0L
        for (stat in stats) {
            if (stat.packageName in monitored) {
                totalMs += stat.totalTimeInForeground
            }
        }
        return (totalMs / 60_000L).toInt().coerceAtLeast(0)
    }

    override fun openUsageAccessSettings() {
        val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        appContext.startActivity(intent)
    }

    private fun resolveGamePackages(): Set<String> {
        val pm = appContext.packageManager
        return pm.getInstalledApplications(0)
            .asSequence()
            .filter { (it.flags and ApplicationInfo.FLAG_SYSTEM) == 0 }
            .filter { app ->
                val category = app.category
                category == ApplicationInfo.CATEGORY_GAME
            }
            .map { it.packageName }
            .toSet()
    }

    private fun startOfTodayMillis(): Long {
        return Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }
}
