package com.controlself.edu.data.screentime

import com.controlself.edu.domain.repository.ScreenTimeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Contador mock. PRP-05 lo reemplazará con UsageStatsManager.
 */
class FakeScreenTimeRepository(
    initialMinutes: Int = 18
) : ScreenTimeRepository {

    private val _minutes = MutableStateFlow(initialMinutes)

    override fun observeTodayMinutes(): Flow<Int> = _minutes.asStateFlow()

    /** Solo para demos / tests hasta PRP-05. */
    fun setTodayMinutes(minutes: Int) {
        _minutes.value = minutes.coerceAtLeast(0)
    }
}
