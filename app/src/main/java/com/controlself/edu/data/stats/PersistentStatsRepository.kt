package com.controlself.edu.data.stats

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.controlself.edu.domain.model.quiz.QuizAttempt
import com.controlself.edu.domain.model.stats.AttemptStat
import com.controlself.edu.domain.repository.StatsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first

private val Context.statsDataStore: DataStore<Preferences> by preferencesDataStore(name = "student_stats")

/**
 * Historial persistente de métricas de intentos (PRP-10).
 */
class PersistentStatsRepository(
    context: Context
) : StatsRepository {

    private val dataStore = context.applicationContext.statsDataStore
    private val _attempts = MutableStateFlow<List<AttemptStat>>(emptyList())

    override fun observeAttempts(): Flow<List<AttemptStat>> = _attempts.asStateFlow()

    suspend fun restore() {
        _attempts.value = readAll(dataStore.data.first())
    }

    override suspend fun recordAttempt(attempt: QuizAttempt) {
        val stat = AttemptStat(
            id = attempt.id,
            courseId = attempt.courseId,
            courseTitle = attempt.courseTitle,
            correctCount = attempt.correctCount,
            total = attempt.total,
            passed = attempt.passed,
            timestampMillis = attempt.timestampMillis,
            durationMillis = attempt.durationMillis
        )
        dataStore.edit { prefs ->
            val current = prefs[KEY_ATTEMPTS].orEmpty().toMutableSet()
            current.add(encode(stat))
            // Mantener tamaño acotado
            if (current.size > MAX_STORED) {
                val trimmed = current
                    .mapNotNull { decode(it) }
                    .sortedByDescending { it.timestampMillis }
                    .take(MAX_STORED)
                    .map { encode(it) }
                    .toSet()
                prefs[KEY_ATTEMPTS] = trimmed
            } else {
                prefs[KEY_ATTEMPTS] = current
            }
        }
        _attempts.value = readAll(dataStore.data.first())
    }

    private fun readAll(prefs: Preferences): List<AttemptStat> =
        prefs[KEY_ATTEMPTS].orEmpty()
            .mapNotNull { decode(it) }
            .sortedBy { it.timestampMillis }

    private fun encode(stat: AttemptStat): String =
        listOf(
            stat.id,
            stat.courseId,
            escape(stat.courseTitle),
            stat.correctCount.toString(),
            stat.total.toString(),
            if (stat.passed) "1" else "0",
            stat.timestampMillis.toString(),
            stat.durationMillis.toString()
        ).joinToString(SEP)

    private fun decode(raw: String): AttemptStat? {
        val parts = raw.split(SEP)
        if (parts.size < 8) return null
        return AttemptStat(
            id = parts[0],
            courseId = parts[1],
            courseTitle = unescape(parts[2]),
            correctCount = parts[3].toIntOrNull() ?: return null,
            total = parts[4].toIntOrNull() ?: return null,
            passed = parts[5] == "1",
            timestampMillis = parts[6].toLongOrNull() ?: return null,
            durationMillis = parts[7].toLongOrNull() ?: 0L
        )
    }

    private fun escape(value: String): String =
        value.replace(SEP, "·")

    private fun unescape(value: String): String = value

    companion object {
        private const val SEP = "|"
        private const val MAX_STORED = 100
        private val KEY_ATTEMPTS = stringSetPreferencesKey("attempt_stats")
    }
}
