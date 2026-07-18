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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.controlself.edu.di.LocalAppContainer
import com.controlself.edu.domain.model.quiz.QuizAttempt
import com.controlself.edu.ui.theme.CseBlue
import com.controlself.edu.ui.theme.CseDanger
import com.controlself.edu.ui.theme.CseGreen
import com.controlself.edu.ui.theme.CseMuted
import com.controlself.edu.ui.theme.CseSurface

/**
 * Resultado mínimo (PRP-07). Copy/detalle enriquecidos en PRP-08.
 */
@Composable
fun QuizResultScreen(
    attemptId: String,
    onRetry: (courseId: String) -> Unit,
    onHome: () -> Unit,
    onBackToLock: () -> Unit
) {
    val attempt = LocalAppContainer.current.quizAttemptRepository.getById(attemptId)
    var showDetail by remember { mutableStateOf(false) }

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
            Button(onClick = onHome) { Text("Volver al inicio") }
        }
        return
    }

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
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Has obtenido ${attempt.correctCount}/${attempt.total} respuestas correctas",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Aplicaciones desbloqueadas.",
                style = MaterialTheme.typography.bodyLarge,
                color = CseBlue,
                fontWeight = FontWeight.SemiBold
            )
        } else {
            Text(
                text = "¡Sigue intentándolo!",
                style = MaterialTheme.typography.headlineMedium,
                color = CseDanger,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Obtuviste ${attempt.correctCount}/${attempt.total} respuestas correctas",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Necesitas al menos ${QuizAttempt.PASS_THRESHOLD} correctas. " +
                    "Revisa tus errores y vuelve a intentarlo.",
                style = MaterialTheme.typography.bodyLarge,
                color = CseMuted
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
        OutlinedButton(
            onClick = { showDetail = !showDetail },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (showDetail) "Ocultar resultados" else "Ver resultados")
        }

        if (showDetail) {
            Spacer(modifier = Modifier.height(16.dp))
            attempt.answers.forEachIndexed { i, a ->
                Text(
                    text = "${i + 1}. ${a.prompt}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Tu respuesta: ${a.userAnswerLabel}" +
                        if (a.isCorrect) " ✓" else " ✗ (correcta: ${a.correctAnswerLabel})",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (a.isCorrect) CseGreen else CseDanger
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
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
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = attempt.courseTitle,
            style = MaterialTheme.typography.bodySmall,
            color = CseMuted,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
