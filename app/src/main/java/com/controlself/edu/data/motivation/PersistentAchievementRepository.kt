package com.controlself.edu.data.motivation

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.controlself.edu.domain.model.motivation.AchievementId
import com.controlself.edu.domain.model.motivation.MotivationSnapshot
import com.controlself.edu.domain.model.motivation.StreakState
import com.controlself.edu.domain.model.motivation.UnlockedAchievement
import com.controlself.edu.domain.model.quiz.QuizAttempt
import com.controlself.edu.domain.repository.AchievementRepository
import com.controlself.edu.domain.repository.ScreenTimeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val Context.motivationDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "motivation"
)

/**
 * Persistencia de racha, stats y logros (PRP-09). Sin farmear: cada AchievementId una vez.
 */
class PersistentAchievementRepository(
    context: Context
) : AchievementRepository {

    private val dataStore = context.applicationContext.motivationDataStore
    private val _snapshot = MutableStateFlow(MotivationSnapshot())

    override fun observeSnapshot(): Flow<MotivationSnapshot> = _snapshot.asStateFlow()

    suspend fun restore() {
        _snapshot.value = readSnapshot(dataStore.data.first())
        refreshStreakIfBroken()
    }

    override suspend fun onQuizAttempt(attempt: QuizAttempt) {
        val today = todayKey()
        dataStore.edit { prefs ->
            val completed = (prefs[KEY_COMPLETED] ?: 0) + 1
            prefs[KEY_COMPLETED] = completed

            val scoreSum = (prefs[KEY_SCORE_SUM] ?: 0.0) + attempt.correctCount
            prefs[KEY_SCORE_SUM] = scoreSum

            if (attempt.passed) {
                prefs[KEY_PASSED] = (prefs[KEY_PASSED] ?: 0) + 1
            }

            val streak = computeStreak(
                currentDays = prefs[KEY_STREAK_DAYS] ?: 0,
                lastDay = prefs[KEY_STREAK_LAST_DAY],
                today = today
            )
            prefs[KEY_STREAK_DAYS] = streak.currentDays
            prefs[KEY_STREAK_LAST_DAY] = today
            val maxStreak = maxOf(prefs[KEY_MAX_STREAK] ?: 0, streak.currentDays)
            prefs[KEY_MAX_STREAK] = maxStreak

            unlockIfNeeded(prefs, evaluateNewUnlocks(prefs))
        }
        _snapshot.value = readSnapshot(dataStore.data.first())
    }

    override suspend fun syncResponsibleUsage(minutesUsed: Int, isForceLocked: Boolean) {
        val today = todayKey()
        dataStore.edit { prefs ->
            val forceDays = prefs[KEY_FORCE_LOCK_DAYS].orEmpty().toMutableSet()
            val responsibleDays = prefs[KEY_RESPONSIBLE_DAYS].orEmpty().toMutableSet()

            if (isForceLocked) {
                forceDays.add(today)
                responsibleDays.remove(today)
            } else if (minutesUsed < ScreenTimeRepository.DAILY_LIMIT_MINUTES) {
                if (today !in forceDays) {
                    responsibleDays.add(today)
                }
            }

            prefs[KEY_FORCE_LOCK_DAYS] = forceDays
            prefs[KEY_RESPONSIBLE_DAYS] = responsibleDays

            unlockIfNeeded(prefs, evaluateNewUnlocks(prefs))
        }
        refreshStreakIfBroken()
        _snapshot.value = readSnapshot(dataStore.data.first())
    }

    /** Si el último día activo no es hoy ni ayer, la racha se muestra en 0. */
    private suspend fun refreshStreakIfBroken() {
        val today = todayKey()
        dataStore.edit { prefs ->
            val last = prefs[KEY_STREAK_LAST_DAY] ?: return@edit
            val yesterday = LocalDate.now().minusDays(1)
                .format(DateTimeFormatter.ISO_LOCAL_DATE)
            if (last != today && last != yesterday) {
                prefs[KEY_STREAK_DAYS] = 0
            }
        }
        _snapshot.value = readSnapshot(dataStore.data.first())
    }

    private fun computeStreak(currentDays: Int, lastDay: String?, today: String): StreakState {
        if (lastDay == today) {
            return StreakState(
                currentDays = currentDays.coerceAtLeast(1),
                lastActivityDay = today
            )
        }
        val yesterday = LocalDate.now().minusDays(1)
            .format(DateTimeFormatter.ISO_LOCAL_DATE)
        val next = when {
            lastDay == null -> 1
            lastDay == yesterday -> currentDays + 1
            else -> 1
        }
        return StreakState(currentDays = next, lastActivityDay = today)
    }

    private fun evaluateNewUnlocks(prefs: Preferences): List<AchievementId> {
        val already = prefs[KEY_UNLOCKED_IDS].orEmpty()
        val streak = prefs[KEY_STREAK_DAYS] ?: 0
        val completed = prefs[KEY_COMPLETED] ?: 0
        val passed = prefs[KEY_PASSED] ?: 0
        val scoreSum = prefs[KEY_SCORE_SUM] ?: 0.0
        val average = if (completed > 0) scoreSum / completed else null
        val responsibleCount = prefs[KEY_RESPONSIBLE_DAYS].orEmpty().size

        val candidates = buildList {
            if (completed >= 1) add(AchievementId.BEGINNER)
            if (streak >= AchievementRepository.CONSISTENT_STREAK) add(AchievementId.CONSISTENT)
            if (streak >= AchievementRepository.OUTSTANDING_STREAK ||
                (average != null && average >= AchievementRepository.OUTSTANDING_AVERAGE)
            ) {
                add(AchievementId.OUTSTANDING)
            }
            if (streak >= AchievementRepository.MASTER_STREAK ||
                passed >= AchievementRepository.MASTER_PASSED
            ) {
                add(AchievementId.MASTER)
            }
            if (responsibleCount >= AchievementRepository.RESPONSIBLE_DAYS_REQUIRED) {
                add(AchievementId.RESPONSIBLE)
            }
        }
        return candidates.filter { it.name !in already }
    }

    private fun unlockIfNeeded(prefs: MutablePreferences, newIds: List<AchievementId>) {
        if (newIds.isEmpty()) return
        val now = System.currentTimeMillis()
        val ids = prefs[KEY_UNLOCKED_IDS].orEmpty().toMutableSet()
        newIds.forEach { id ->
            ids.add(id.name)
            prefs[unlockAtKey(id)] = now
        }
        prefs[KEY_UNLOCKED_IDS] = ids
    }

    private fun readSnapshot(prefs: Preferences): MotivationSnapshot {
        val completed = prefs[KEY_COMPLETED] ?: 0
        val passed = prefs[KEY_PASSED] ?: 0
        val scoreSum = prefs[KEY_SCORE_SUM] ?: 0.0
        val unlockedIds = prefs[KEY_UNLOCKED_IDS].orEmpty()
        val unlocked = unlockedIds.mapNotNull { name ->
            val id = runCatching { AchievementId.valueOf(name) }.getOrNull() ?: return@mapNotNull null
            UnlockedAchievement(
                id = id,
                unlockedAtMillis = prefs[unlockAtKey(id)] ?: 0L
            )
        }.sortedBy { it.unlockedAtMillis }

        return MotivationSnapshot(
            streak = StreakState(
                currentDays = prefs[KEY_STREAK_DAYS] ?: 0,
                lastActivityDay = prefs[KEY_STREAK_LAST_DAY]
            ),
            maxStreakDays = prefs[KEY_MAX_STREAK] ?: 0,
            unlocked = unlocked,
            completedAttempts = completed,
            passedAttempts = passed,
            averageScore = if (completed > 0) scoreSum / completed else null
        )
    }

    private fun todayKey(): String =
        LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)

    private fun unlockAtKey(id: AchievementId) =
        longPreferencesKey("unlock_at_${id.name}")

    companion object {
        private val KEY_STREAK_DAYS = intPreferencesKey("streak_days")
        private val KEY_STREAK_LAST_DAY = stringPreferencesKey("streak_last_day")
        private val KEY_MAX_STREAK = intPreferencesKey("max_streak_days")
        private val KEY_COMPLETED = intPreferencesKey("completed_attempts")
        private val KEY_PASSED = intPreferencesKey("passed_attempts")
        private val KEY_SCORE_SUM = doublePreferencesKey("score_sum")
        private val KEY_UNLOCKED_IDS = stringSetPreferencesKey("unlocked_ids")
        private val KEY_RESPONSIBLE_DAYS = stringSetPreferencesKey("responsible_days")
        private val KEY_FORCE_LOCK_DAYS = stringSetPreferencesKey("force_lock_days")
    }
}
