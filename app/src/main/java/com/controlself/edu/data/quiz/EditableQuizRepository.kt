package com.controlself.edu.data.quiz

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.controlself.edu.domain.model.Course
import com.controlself.edu.domain.model.quiz.Question
import com.controlself.edu.domain.model.quiz.QuestionType
import com.controlself.edu.domain.model.quiz.QuizAttempt
import com.controlself.edu.domain.model.teacher.CourseBankStatus
import com.controlself.edu.domain.repository.QuizRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

private val Context.quizBankDataStore: DataStore<Preferences> by preferencesDataStore(name = "quiz_bank")

/**
 * Banco editable (PRP-11): semilla [QuizBank] + overrides DataStore por curso.
 */
class EditableQuizRepository(
    context: Context
) : QuizRepository {

    private val dataStore = context.applicationContext.quizBankDataStore
    private val cache = MutableStateFlow<Map<String, List<Question>>>(emptyMap())

    suspend fun restore() {
        val prefs = dataStore.data.first()
        val map = Course.entries.associate { course ->
            val stored = prefs[keyFor(course.id)]
            val questions = if (stored.isNullOrEmpty()) {
                QuizBank.seedQuestions(course.id)
            } else {
                stored.mapNotNull { decode(it) }.ifEmpty { QuizBank.seedQuestions(course.id) }
            }
            course.id to questions
        }
        cache.value = map
    }

    override fun questionsFor(courseId: String): List<Question> {
        val cached = cache.value[courseId]
        if (cached != null) return cached
        return QuizBank.seedQuestions(courseId)
    }

    override fun observeQuestions(courseId: String): Flow<List<Question>> =
        cache.asStateFlow().map { it[courseId] ?: QuizBank.seedQuestions(courseId) }

    override fun observeBankStatus(): Flow<List<CourseBankStatus>> =
        cache.asStateFlow().map { map ->
            Course.entries.map { course ->
                val count = (map[course.id] ?: QuizBank.seedQuestions(course.id)).size
                CourseBankStatus(
                    courseId = course.id,
                    courseTitle = course.title,
                    questionCount = count,
                    isReady = count == QuizAttempt.TOTAL_QUESTIONS
                )
            }
        }

    override suspend fun upsertQuestion(courseId: String, question: Question) {
        val current = questionsFor(courseId).toMutableList()
        val index = current.indexOfFirst { it.id == question.id }
        if (index >= 0) current[index] = question else current.add(question)
        persist(courseId, current)
    }

    override suspend fun deleteQuestion(courseId: String, questionId: String) {
        val current = questionsFor(courseId).filterNot { it.id == questionId }
        persist(courseId, current)
    }

    override suspend fun replaceAll(courseId: String, questions: List<Question>) {
        persist(courseId, questions)
    }

    private suspend fun persist(courseId: String, questions: List<Question>) {
        dataStore.edit { prefs ->
            prefs[keyFor(courseId)] = questions.map { encode(it) }.toSet()
        }
        cache.update { it + (courseId to questions) }
    }

    private fun keyFor(courseId: String) = stringSetPreferencesKey("questions_$courseId")

    private fun encode(q: Question): String =
        listOf(
            q.id,
            q.type.name,
            escape(q.prompt),
            q.options.joinToString(";") { escape(it) },
            q.correctIndex.toString()
        ).joinToString(SEP)

    private fun decode(raw: String): Question? {
        val parts = raw.split(SEP)
        if (parts.size < 5) return null
        val type = runCatching { QuestionType.valueOf(parts[1]) }.getOrNull() ?: return null
        val options = parts[3].split(";").map { unescape(it) }
        return Question(
            id = parts[0],
            prompt = unescape(parts[2]),
            type = type,
            options = options,
            correctIndex = parts[4].toIntOrNull() ?: return null
        )
    }

    private fun escape(value: String): String =
        value.replace(SEP, "·").replace(";", "‚")

    private fun unescape(value: String): String =
        value.replace("·", SEP).replace("‚", ";")

    companion object {
        private const val SEP = "|"
    }
}
