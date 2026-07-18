package com.controlself.edu.ui.screens.quiz

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.controlself.edu.di.LocalAppContainer
import com.controlself.edu.domain.model.quiz.QuizAttempt
import com.controlself.edu.ui.preview.PreviewSamples
import com.controlself.edu.ui.theme.ControlSelfEDUTheme
import com.controlself.edu.ui.theme.CseBlue
import com.controlself.edu.ui.theme.CseDanger
import com.controlself.edu.ui.theme.CseGreen
import com.controlself.edu.ui.theme.CseMuted
import com.controlself.edu.ui.theme.CseSurface

/**
 * Resultado de evaluación (PRP-08): copy del brief, desbloqueo / reintento.
 */
@Composable
fun QuizResultScreen(
    attemptId: String,
    onReview: (attemptId: String) -> Unit,
    onRetry: (courseId: String) -> Unit,
    onHome: () -> Unit,
    onBackToLock: () -> Unit
) {
    val attempt = LocalAppContainer.current.quizAttemptRepository.getById(attemptId)
    QuizResultContent(
        attempt = attempt,
        onReview = onReview,
        onRetry = onRetry,
        onHome = onHome,
        onBackToLock = onBackToLock
    )
}

@Composable
private fun QuizResultContent(
    attempt: QuizAttempt?,
    onReview: (attemptId: String) -> Unit,
    onRetry: (courseId: String) -> Unit,
    onHome: () -> Unit,
    onBackToLock: () -> Unit
) {
    BackHandler {
        if (attempt?.passed == true) onHome() else onBackToLock()
    }

    if (attempt == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(CseSurface)
                .padding(24.dp)
        ) {
            Text("Intento no encontrado", color = CseDanger)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onBackToLock) { Text("Volver") }
        }
        return
    }

    val wrongCount = attempt.answers.count { !it.isCorrect }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CseSurface)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        if (attempt.passed) {
            Text(
                text = "¡Felicitaciones!",
                style = MaterialTheme.typography.headlineMedium,
                color = CseGreen,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Has obtenido ${attempt.correctCount}/20 respuestas correctas",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Aplicaciones desbloqueadas",
                style = MaterialTheme.typography.bodyLarge,
                color = CseBlue,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Puedes usar redes y juegos el resto del día.",
                style = MaterialTheme.typography.bodyMedium,
                color = CseMuted
            )
        } else {
            Text(
                text = "¡Sigue intentándolo!",
                style = MaterialTheme.typography.headlineMedium,
                color = CseDanger,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Obtuviste ${attempt.correctCount}/20",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Necesitas al menos ${QuizAttempt.PASS_THRESHOLD} correctas. " +
                    "Erraste $wrongCount pregunta${if (wrongCount == 1) "" else "s"}. " +
                    "Revisa las respuestas correctas y vuelve a resolver la misma evaluación.",
                style = MaterialTheme.typography.bodyLarge,
                color = CseMuted
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        OutlinedButton(
            onClick = { onReview(attempt.id) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ver resultados")
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (attempt.passed) {
            Button(onClick = onHome, modifier = Modifier.fillMaxWidth()) {
                Text("Volver al inicio")
            }
        } else {
            Button(
                onClick = { onRetry(attempt.courseId) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Reintentar")
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(onClick = onBackToLock, modifier = Modifier.fillMaxWidth()) {
                Text("Volver al bloqueo")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = attempt.courseTitle,
            style = MaterialTheme.typography.bodySmall,
            color = CseMuted,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true, name = "Resultado aprobado")
@Composable
private fun QuizResultScreenPassedPreview() {
    ControlSelfEDUTheme {
        QuizResultContent(
            attempt = PreviewSamples.quizAttemptPassed,
            onReview = {},
            onRetry = {},
            onHome = {},
            onBackToLock = {}
        )
    }
}

@Preview(showBackground = true, name = "Resultado reprobado")
@Composable
private fun QuizResultScreenFailedPreview() {
    ControlSelfEDUTheme {
        QuizResultContent(
            attempt = PreviewSamples.quizAttemptFailed,
            onReview = {},
            onRetry = {},
            onHome = {},
            onBackToLock = {}
        )
    }
}
