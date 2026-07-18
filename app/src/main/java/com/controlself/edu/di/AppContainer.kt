package com.controlself.edu.di

import android.content.Context
import com.controlself.edu.data.auth.InMemoryAuthRepository
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

/**
 * DI manual (decisión PRP-01). Sin Hilt/Koin en MVP.
 */
class AppContainer(
    @Suppress("unused") private val appContext: Context
) {
    val authRepository: AuthRepository = InMemoryAuthRepository()
    val screenTimeRepository: ScreenTimeRepository = FakeScreenTimeRepository()
    val lockRepository: LockRepository = InMemoryLockRepository()

    val usageStatsGateway: UsageStatsGateway = NoOpUsageStatsGateway()
    val entertainmentLockController: EntertainmentLockController =
        NoOpEntertainmentLockController()
    val deviceAdminGateway: DeviceAdminGateway = NoOpDeviceAdminGateway()
}
