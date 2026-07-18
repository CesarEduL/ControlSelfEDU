package com.controlself.edu.data.lock

import com.controlself.edu.domain.repository.LockRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class InMemoryLockRepository : LockRepository {

    private val _locked = MutableStateFlow(false)

    override fun observeLocked(): Flow<Boolean> = _locked.asStateFlow()

    override suspend fun setLocked(locked: Boolean) {
        _locked.value = locked
    }
}
