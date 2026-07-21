package com.controlself.edu.ui.screens.quiz

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
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
import com.controlself.edu.ui.components.PrimaryFlatButton
import com.controlself.edu.ui.components.QuestionPromptMedia
import com.controlself.edu.ui.theme.ControlSelfEDUTheme
import com.controlself.edu.ui.theme.CseBackground
import com.controlself.edu.ui.theme.CseOnSurface
import com.controlself.edu.ui.theme.CseOnSurfaceVariant
import com.controlself.edu.ui.theme.CseOutlineVariant
import com.controlself.edu.ui.theme.CsePrimary
import com.controlself.edu.ui.theme.CseSecondary
import com.controlself.edu.ui.theme.CseSecondaryContainer
import com.controlself.edu.ui.theme.CseSurfaceContainer
import com.controlself.edu.ui.theme.CseWhite
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
            container.questionAnalyticsRepository.recordAttemptAnswers(attempt)
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
                }) { Text("Salir", color = CsePrimary) }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) {
                    Text("Continuar", color = CseSecondary)
                }
            },
            containerColor = CseWhite
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CseBackground)
            .padding(16.dp)
    ) {
        IconButton(onClick = { requestExit() }) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Salir",
                tint = CsePrimary
            )
        }
        Text(
            text = course.title,
            style = MaterialTheme.typography.titleLarge,
            color = CsePrimary,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Pregunta ${index + 1} de ${questions.size}",
            style = MaterialTheme.typography.bodyMedium,
            color = CseOnSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = CseSecondary,
            trackColor = CseSurfaceContainer,
            strokeCap = StrokeCap.Round
        )
        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(16.dp))
                .background(CseWhite)
                .border(1.dp, CseOutlineVariant, RoundedCornerShape(16.dp))
                .padding(20.dp)
        ) {
            QuestionPromptMedia(
                prompt = current.prompt,
                imagePath = current.imagePath,
                promptPlaceholder = current.prompt
            )
            Spacer(modifier = Modifier.height(20.dp))
            current.options.forEachIndexed { optIndex, label ->
                val isSelected = selected == optIndex
                OptionChip(
                    label = label,
                    selected = isSelected,
                    onClick = { selections[index] = optIndex },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        PrimaryFlatButton(
            text = if (index < questions.lastIndex) "Siguiente" else "Finalizar",
            onClick = {
                if (selected == null) return@PrimaryFlatButton
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
            containerColor = CseSecondary
        )
    }
}

@Composable
private fun OptionChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bg = if (selected) CseSecondaryContainer else CseBackground
    val border = if (selected) CseSecondary else CseOutlineVariant
    val textColor = if (selected) CsePrimary else CseOnSurface

    Text(
        text = label,
        style = MaterialTheme.typography.bodyLarge,
        color = textColor,
        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bg)
            .border(BorderStroke(2.dp, border), RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp)
    )
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
