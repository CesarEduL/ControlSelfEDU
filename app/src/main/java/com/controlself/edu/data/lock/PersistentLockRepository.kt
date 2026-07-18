package com.controlself.edu.data.lock

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.controlself.edu.domain.repository.LockRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first

private val Context.lockDataStore: DataStore<Preferences> by preferencesDataStore(name = "entertainment_lock")

/**
 * Lock persistente (PRP-06). Sobrevive reinicios de la app.
 */
class PersistentLockRepository(
    context: Context
) : LockRepository {

    private val dataStore = context.applicationContext.lockDataStore
    private val _locked = MutableStateFlow(false)

    override fun observeLocked(): Flow<Boolean> = _locked.asStateFlow()

    suspend fun restore() {
        _locked.value = dataStore.data.first()[KEY_LOCKED] == true
    }

    override suspend fun setLocked(locked: Boolean) {
        _locked.value = locked
        dataStore.edit { it[KEY_LOCKED] = locked }
    }

    companion object {
        private val KEY_LOCKED = booleanPreferencesKey("entertainment_locked")
    }
}
