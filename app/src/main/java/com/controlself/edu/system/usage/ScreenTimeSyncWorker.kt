package com.controlself.edu.system.usage

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.controlself.edu.ControlSelfApplication
import java.util.concurrent.TimeUnit

class ScreenTimeSyncWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val app = applicationContext as? ControlSelfApplication
            ?: return Result.retry()
        return try {
            app.container.screenTimeRepository.refresh()
            Result.success()
        } catch (_: Exception) {
            Result.retry()
        }
    }

    companion object {
        private const val UNIQUE_NAME = "screen_time_sync"

        fun enqueue(context: Context) {
            val request = PeriodicWorkRequestBuilder<ScreenTimeSyncWorker>(
                15,
                TimeUnit.MINUTES
            ).build()
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                UNIQUE_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
        }
    }
}
