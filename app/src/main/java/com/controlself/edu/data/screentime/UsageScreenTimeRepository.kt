package com.controlself.edu.data.screentime

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
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
 * Historial 7 días para panel padre (PRP-12).
 */
class UsageScreenTimeRepository(
    context: Context,
    private val usageStatsGateway: UsageStatsGateway,
    private val lockRepository: LockRepository
) : ScreenTimeRepository {

    private val dataStore = context.applicationContext.screenTimeDataStore

    private val _minutes = MutableStateFlow(0)
    private val _permission = MutableStateFlow(false)
    private val _weekly = MutableStateFlow<Map<String, Int>>(emptyMap())

    override fun observeTodayMinutes(): Flow<Int> = _minutes.asStateFlow()

    override fun observeUsagePermissionGranted(): Flow<Boolean> = _permission.asStateFlow()

    override fun observeWeeklyEntertainmentMinutes(): Flow<Map<String, Int>> = _weekly.asStateFlow()

    suspend fun bootstrap() {
        val prefs = dataStore.data.first()
        val today = todayKey()
        val storedDay = prefs[KEY_DAY]
        val storedMinutes = prefs[KEY_MINUTES] ?: 0
        _weekly.value = readWeekly(prefs)
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
            _weekly.value = readWeekly(dataStore.data.first())
            return
        }

        val queried = usageStatsGateway.queryEntertainmentMinutesToday() ?: 0
        _minutes.value = queried
        persist(today, queried)

        if (queried >= ScreenTimeRepository.DAILY_LIMIT_MINUTES &&
            !lockRepository.hasRestOfDayPass()
        ) {
            lockRepository.setLocked(true)
        }
    }

    private suspend fun persist(day: String, minutes: Int) {
        dataStore.edit { prefs ->
            prefs[KEY_DAY] = day
            prefs[KEY_MINUTES] = minutes
            val history = prefs[KEY_WEEKLY].orEmpty().toMutableSet()
            history.removeAll { it.startsWith("$day=") }
            history.add("$day=$minutes")
            val trimmed = history
                .mapNotNull { entry ->
                    val parts = entry.split("=")
                    if (parts.size != 2) return@mapNotNull null
                    val d = runCatching { LocalDate.parse(parts[0]) }.getOrNull() ?: return@mapNotNull null
                    val m = parts[1].toIntOrNull() ?: return@mapNotNull null
                    d to m
                }
                .sortedByDescending { it.first }
                .take(14)
                .map { "${it.first.format(DateTimeFormatter.ISO_LOCAL_DATE)}=${it.second}" }
                .toSet()
            prefs[KEY_WEEKLY] = trimmed
        }
        _weekly.value = readWeekly(dataStore.data.first())
    }

    private fun readWeekly(prefs: Preferences): Map<String, Int> =
        prefs[KEY_WEEKLY].orEmpty().mapNotNull { entry ->
            val parts = entry.split("=")
            if (parts.size != 2) return@mapNotNull null
            val minutes = parts[1].toIntOrNull() ?: return@mapNotNull null
            parts[0] to minutes
        }.toMap()

    private fun todayKey(): String =
        LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)

    companion object {
        private val KEY_DAY = stringPreferencesKey("day")
        private val KEY_MINUTES = intPreferencesKey("minutes")
        private val KEY_WEEKLY = stringSetPreferencesKey("weekly_entertainment")
    }
}
