package com.controlself.edu.data.lock

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.controlself.edu.domain.repository.LockRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val Context.lockDataStore: DataStore<Preferences> by preferencesDataStore(name = "entertainment_lock")

/**
 * Lock persistente (PRP-06) + pase del resto del día tras aprobar (PRP-08).
 */
class PersistentLockRepository(
    context: Context
) : LockRepository {

    private val dataStore = context.applicationContext.lockDataStore
    private val _locked = MutableStateFlow(false)

    override fun observeLocked(): Flow<Boolean> = _locked.asStateFlow()

    suspend fun restore() {
        val prefs = dataStore.data.first()
        _locked.value = prefs[KEY_LOCKED] == true
        // Si el pase es de otro día, se ignora (hasRestOfDayPass lo valida).
    }

    override suspend fun setLocked(locked: Boolean) {
        _locked.value = locked
        dataStore.edit { prefs ->
            prefs[KEY_LOCKED] = locked
            if (locked) {
                // Simular bloqueo / nuevo ciclo: quita el pase del día.
                prefs.remove(KEY_DAY_PASS)
            }
        }
    }

    override suspend fun unlockForRestOfDay() {
        _locked.value = false
        val today = todayKey()
        dataStore.edit { prefs ->
            prefs[KEY_LOCKED] = false
            prefs[KEY_DAY_PASS] = today
        }
    }

    override suspend fun hasRestOfDayPass(): Boolean {
        val passDay = dataStore.data.first()[KEY_DAY_PASS] ?: return false
        return passDay == todayKey()
    }

    private fun todayKey(): String =
        LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)

    companion object {
        private val KEY_LOCKED = booleanPreferencesKey("entertainment_locked")
        private val KEY_DAY_PASS = stringPreferencesKey("unlock_day_pass")
    }
}
