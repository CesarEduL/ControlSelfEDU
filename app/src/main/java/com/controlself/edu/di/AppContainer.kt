package com.controlself.edu.di

import android.content.Context
import com.controlself.edu.data.auth.LocalAuthRepository
import com.controlself.edu.data.lock.InMemoryLockRepository
import com.controlself.edu.data.screentime.FakeScreenTimeRepository
import com.controlself.edu.domain.repository.AuthRepository
import com.controlself.edu.domain.repository.LockRepository
import com.controlself.edu.domain.repository.ScreenTimeRepository
import com.controlself.edu.system.admin.DeviceAdminGateway
import com.controlself.edu.system.admin.NoOpDeviceAdminGateway
import com.controlself.edu.system.lock.EntertainmentLockController
import com.controlself.edu.system.lock.NoOpEntertainmentLockController
import com.controlself.edu.system.usage.NoOpUsageStatsGateway
import com.controlself.edu.system.usage.UsageStatsGateway
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * DI manual (PRP-01). Auth local con DataStore (PRP-03).
 */
class AppContainer(
    appContext: Context
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val localAuth = LocalAuthRepository(appContext.applicationContext)

    val authRepository: AuthRepository = localAuth
    val screenTimeRepository: ScreenTimeRepository = FakeScreenTimeRepository()
    val lockRepository: LockRepository = InMemoryLockRepository()

    val usageStatsGateway: UsageStatsGateway = NoOpUsageStatsGateway()
    val entertainmentLockController: EntertainmentLockController =
        NoOpEntertainmentLockController()
    val deviceAdminGateway: DeviceAdminGateway = NoOpDeviceAdminGateway()

    init {
        scope.launch { localAuth.restore() }
    }
}
