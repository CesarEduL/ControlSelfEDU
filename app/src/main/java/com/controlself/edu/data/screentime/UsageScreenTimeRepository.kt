package com.controlself.edu.data.screentime

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.controlself.edu.domain.repository.LockRepository
import com.controlself.edu.domain.repository.ScreenTimeRepository
import com.controlself.edu.system.usage.UsageStatsGateway
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val Context.screenTimeDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "screen_time"
)

/**
 * Contador diario desde UsageStats + caché DataStore (PRP-05).
 */
class UsageScreenTimeRepository(
    context: Context,
    private val usageStatsGateway: UsageStatsGateway,
    private val lockRepository: LockRepository
) : ScreenTimeRepository {

    private val dataStore = context.applicationContext.screenTimeDataStore

    private val _minutes = MutableStateFlow(0)
    private val _permission = MutableStateFlow(false)

    override fun observeTodayMinutes(): Flow<Int> = _minutes.asStateFlow()

    override fun observeUsagePermissionGranted(): Flow<Boolean> = _permission.asStateFlow()

    suspend fun bootstrap() {
        val prefs = dataStore.data.first()
        val today = todayKey()
        val storedDay = prefs[KEY_DAY]
        val storedMinutes = prefs[KEY_MINUTES] ?: 0
        if (storedDay != today) {
            _minutes.value = 0
            persist(today, 0)
        } else {
            _minutes.value = storedMinutes
        }
        _permission.value = usageStatsGateway.hasUsageAccessPermission()
        refresh()
    }

    override suspend fun refresh() {
        val granted = usageStatsGateway.hasUsageAccessPermission()
        _permission.value = granted

        val today = todayKey()
        val prefs = dataStore.data.first()
        if (prefs[KEY_DAY] != today) {
            _minutes.value = 0
            persist(today, 0)
        }

        if (!granted) {
            // Sin permiso no inventamos minutos; mantenemos caché del día o 0.
            return
        }

        val queried = usageStatsGateway.queryEntertainmentMinutesToday() ?: 0
        _minutes.value = queried
        persist(today, queried)

        if (queried >= ScreenTimeRepository.DAILY_LIMIT_MINUTES) {
            lockRepository.setLocked(true)
        }
    }

    private suspend fun persist(day: String, minutes: Int) {
        dataStore.edit { prefs ->
            prefs[KEY_DAY] = day
            prefs[KEY_MINUTES] = minutes
        }
    }

    private fun todayKey(): String =
        LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)

    companion object {
        private val KEY_DAY = stringPreferencesKey("day")
        private val KEY_MINUTES = intPreferencesKey("minutes")
    }
}
