package com.controlself.edu.ui.screens.quiz

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.controlself.edu.data.quiz.QuizBank
import com.controlself.edu.di.LocalAppContainer
import com.controlself.edu.domain.model.Course
import com.controlself.edu.domain.model.quiz.AnswerRecord
import com.controlself.edu.domain.model.quiz.Question
import com.controlself.edu.domain.model.quiz.QuizAttempt
import com.controlself.edu.ui.theme.ControlSelfEDUTheme
import com.controlself.edu.ui.theme.CseBlue
import com.controlself.edu.ui.theme.CseMuted
import com.controlself.edu.ui.theme.CseSurface
import kotlinx.coroutines.launch
import java.util.UUID

@Composable
fun QuizPlayScreen(
    course: Course,
    onFinished: (attemptId: String) -> Unit,
    onAbort: () -> Unit
) {
    val container = LocalAppContainer.current
    val questions = remember(course.id) {
        container.quizRepository.questionsFor(course.id)
    }

    QuizPlayContent(
        course = course,
        questions = questions,
        onFinished = { attempt ->
            container.quizAttemptRepository.save(attempt)
            container.achievementRepository.onQuizAttempt(attempt)
            container.statsRepository.recordAttempt(attempt)
            if (attempt.passed) {
                container.lockRepository.unlockForRestOfDay()
            }
            onFinished(attempt.id)
        },
        onAbort = onAbort
    )
}

@Composable
private fun QuizPlayContent(
    course: Course,
    questions: List<Question>,
    onFinished: suspend (QuizAttempt) -> Unit,
    onAbort: () -> Unit
) {
    val selections = remember {
        mutableStateListOf<Int?>().apply {
            repeat(questions.size) { add(null) }
        }
    }
    var index by remember { mutableIntStateOf(0) }
    var showExitDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val startedAtMillis = remember { System.currentTimeMillis() }

    val current = questions[index]
    val selected = selections[index]
    val progress = (index + 1).toFloat() / questions.size

    fun requestExit() {
        showExitDialog = true
    }

    BackHandler { requestExit() }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("¿Salir de la evaluación?") },
            text = { Text("Perderás el progreso de esta lección.") },
            confirmButton = {
                TextButton(onClick = {
                    showExitDialog = false
                    onAbort()
                }) { Text("Salir") }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) { Text("Continuar") }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CseSurface)
            .padding(16.dp)
    ) {
        IconButton(onClick = { requestExit() }) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Salir")
        }
        Text(
            text = course.title,
            style = MaterialTheme.typography.titleLarge,
            color = CseBlue,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Pregunta ${index + 1} de ${questions.size}",
            style = MaterialTheme.typography.bodyMedium,
            color = CseMuted
        )
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = CseBlue,
            strokeCap = StrokeCap.Round
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = current.prompt,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(20.dp))
        current.options.forEachIndexed { optIndex, label ->
            val isSelected = selected == optIndex
            if (isSelected) {
                Button(
                    onClick = { selections[index] = optIndex },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) { Text(label) }
            } else {
                OutlinedButton(
                    onClick = { selections[index] = optIndex },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) { Text(label) }
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = {
                if (selected == null) return@Button
                if (index < questions.lastIndex) {
                    index++
                } else {
                    scope.launch {
                        val answers = questions.mapIndexed { i, q ->
                            val sel = selections[i] ?: -1
                            val userLabel = q.options.getOrNull(sel) ?: "—"
                            val correctLabel = q.options[q.correctIndex]
                            AnswerRecord(
                                questionId = q.id,
                                prompt = q.prompt,
                                userAnswerLabel = userLabel,
                                correctAnswerLabel = correctLabel,
                                isCorrect = sel == q.correctIndex
                            )
                        }
                        val correct = answers.count { it.isCorrect }
                        val attempt = QuizAttempt(
                            id = UUID.randomUUID().toString(),
                            courseId = course.id,
                            courseTitle = course.title,
                            answers = answers,
                            correctCount = correct,
                            durationMillis = System.currentTimeMillis() - startedAtMillis
                        )
                        onFinished(attempt)
                    }
                }
            },
            enabled = selected != null,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (index < questions.lastIndex) "Siguiente" else "Finalizar")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun QuizPlayScreenPreview() {
    ControlSelfEDUTheme {
        QuizPlayContent(
            course = Course.MATH,
            questions = QuizBank.questionsFor(Course.MATH.id),
            onFinished = {},
            onAbort = {}
        )
    }
}
