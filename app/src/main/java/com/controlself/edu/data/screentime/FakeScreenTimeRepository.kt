package com.controlself.edu.data.screentime

import com.controlself.edu.domain.repository.ScreenTimeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Mock para previews / tests. Producción usa [UsageScreenTimeRepository].
 */
class FakeScreenTimeRepository(
    initialMinutes: Int = 18,
    permissionGranted: Boolean = false
) : ScreenTimeRepository {

    private val _minutes = MutableStateFlow(initialMinutes)
    private val _permission = MutableStateFlow(permissionGranted)
    private val _weekly = MutableStateFlow<Map<String, Int>>(emptyMap())

    override fun observeTodayMinutes(): Flow<Int> = _minutes.asStateFlow()

    override fun observeUsagePermissionGranted(): Flow<Boolean> = _permission.asStateFlow()

    override fun observeWeeklyEntertainmentMinutes(): Flow<Map<String, Int>> = _weekly.asStateFlow()

    override suspend fun refresh() = Unit

    fun setTodayMinutes(minutes: Int) {
        _minutes.value = minutes.coerceAtLeast(0)
    }
}
