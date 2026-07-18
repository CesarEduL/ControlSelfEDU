package com.controlself.edu.data.teacher

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.controlself.edu.domain.model.quiz.QuizAttempt
import com.controlself.edu.domain.model.teacher.QuestionDifficulty
import com.controlself.edu.domain.repository.QuestionAnalyticsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first

private val Context.questionAnalyticsDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "question_analytics"
)

/**
 * Agrega aciertos/errores por pregunta a partir de intentos (PRP-11).
 */
class PersistentQuestionAnalyticsRepository(
    context: Context
) : QuestionAnalyticsRepository {

    private val dataStore = context.applicationContext.questionAnalyticsDataStore
    private val _difficulties = MutableStateFlow<List<QuestionDifficulty>>(emptyList())

    override fun observeDifficulties(): Flow<List<QuestionDifficulty>> = _difficulties.asStateFlow()

    suspend fun restore() {
        _difficulties.value = readAll(dataStore.data.first())
    }

    override suspend fun recordAttemptAnswers(attempt: QuizAttempt) {
        dataStore.edit { prefs ->
            val map = prefs[KEY_ROWS].orEmpty()
                .mapNotNull { decode(it) }
                .associateBy { it.questionId + "@" + it.courseId }
                .toMutableMap()

            attempt.answers.forEach { answer ->
                val key = answer.questionId + "@" + attempt.courseId
                val prev = map[key]
                val attempts = (prev?.attempts ?: 0) + 1
                val wrong = (prev?.wrongCount ?: 0) + if (answer.isCorrect) 0 else 1
                map[key] = QuestionDifficulty(
                    questionId = answer.questionId,
                    courseId = attempt.courseId,
                    prompt = answer.prompt,
                    attempts = attempts,
                    wrongCount = wrong
                )
            }
            prefs[KEY_ROWS] = map.values.map { encode(it) }.toSet()
        }
        _difficulties.value = readAll(dataStore.data.first())
    }

    private fun readAll(prefs: Preferences): List<QuestionDifficulty> =
        prefs[KEY_ROWS].orEmpty()
            .mapNotNull { decode(it) }
            .sortedByDescending { it.errorPercent }

    private fun encode(d: QuestionDifficulty): String =
        listOf(
            d.questionId,
            d.courseId,
            escape(d.prompt),
            d.attempts.toString(),
            d.wrongCount.toString()
        ).joinToString(SEP)

    private fun decode(raw: String): QuestionDifficulty? {
        val parts = raw.split(SEP)
        if (parts.size < 5) return null
        return QuestionDifficulty(
            questionId = parts[0],
            courseId = parts[1],
            prompt = unescape(parts[2]),
            attempts = parts[3].toIntOrNull() ?: return null,
            wrongCount = parts[4].toIntOrNull() ?: return null
        )
    }

    private fun escape(value: String): String = value.replace(SEP, "·")
    private fun unescape(value: String): String = value.replace("·", SEP)

    companion object {
        private const val SEP = "|"
        private val KEY_ROWS = stringSetPreferencesKey("difficulty_rows")
    }
}
