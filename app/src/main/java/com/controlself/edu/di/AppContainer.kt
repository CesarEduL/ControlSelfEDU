package com.controlself.edu.di

import android.content.Context
import com.controlself.edu.data.auth.LocalAuthRepository
import com.controlself.edu.data.lock.PersistentLockRepository
import com.controlself.edu.data.motivation.PersistentAchievementRepository
import com.controlself.edu.data.quiz.InMemoryQuizAttemptRepository
import com.controlself.edu.data.quiz.QuizBank
import com.controlself.edu.data.screentime.UsageScreenTimeRepository
import com.controlself.edu.data.stats.PersistentStatsRepository
import com.controlself.edu.domain.repository.AchievementRepository
import com.controlself.edu.domain.repository.AuthRepository
import com.controlself.edu.domain.repository.LockRepository
import com.controlself.edu.domain.repository.QuizAttemptRepository
import com.controlself.edu.domain.repository.QuizRepository
import com.controlself.edu.domain.repository.ScreenTimeRepository
import com.controlself.edu.domain.repository.StatsRepository
import com.controlself.edu.domain.stats.StatsAggregator
import com.controlself.edu.system.admin.DeviceAdminGateway
import com.controlself.edu.system.admin.NoOpDeviceAdminGateway
import com.controlself.edu.system.lock.EntertainmentLockController
import com.controlself.edu.system.lock.NoOpEntertainmentLockController
import com.controlself.edu.system.usage.AndroidUsageStatsGateway
import com.controlself.edu.system.usage.ScreenTimeSyncWorker
import com.controlself.edu.system.usage.UsageStatsGateway
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class AppContainer(
    appContext: Context
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val app = appContext.applicationContext

    private val localAuth = LocalAuthRepository(app)
    private val persistentLock = PersistentLockRepository(app)
    private val persistentAchievements = PersistentAchievementRepository(app)
    private val persistentStats = PersistentStatsRepository(app)
    val usageStatsGateway: UsageStatsGateway = AndroidUsageStatsGateway(app)

    private val usageScreenTime = UsageScreenTimeRepository(
        context = app,
        usageStatsGateway = usageStatsGateway,
        lockRepository = persistentLock
    )

    val authRepository: AuthRepository = localAuth
    val screenTimeRepository: ScreenTimeRepository = usageScreenTime
    val lockRepository: LockRepository = persistentLock
    val quizRepository: QuizRepository = QuizBank
    val quizAttemptRepository: QuizAttemptRepository = InMemoryQuizAttemptRepository()
    val achievementRepository: AchievementRepository = persistentAchievements
    val statsRepository: StatsRepository = persistentStats
    val statsAggregator = StatsAggregator(
        statsRepository = persistentStats,
        screenTimeRepository = usageScreenTime,
        achievementRepository = persistentAchievements
    )

    val entertainmentLockController: EntertainmentLockController =
        NoOpEntertainmentLockController()
    val deviceAdminGateway: DeviceAdminGateway = NoOpDeviceAdminGateway()

    init {
        scope.launch {
            localAuth.restore()
            persistentLock.restore()
            persistentAchievements.restore()
            persistentStats.restore()
            usageScreenTime.bootstrap()
        }
        ScreenTimeSyncWorker.enqueue(app)
    }
}
