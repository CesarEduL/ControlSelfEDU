package com.controlself.edu.ui.screens.parent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.controlself.edu.di.LocalAppContainer
import com.controlself.edu.domain.model.stats.AttemptStat
import com.controlself.edu.ui.theme.CseDanger
import com.controlself.edu.ui.theme.CseGreen
import com.controlself.edu.ui.theme.CseMuted
import com.controlself.edu.ui.theme.CseSurface
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun ParentAttemptDetailScreen(
    attemptId: String,
    onBack: () -> Unit,
    onOpenAnswers: (attemptId: String) -> Unit
) {
    val container = LocalAppContainer.current
    val attempts by container.statsRepository.observeAttempts()
        .collectAsStateWithLifecycle(initialValue = emptyList())
    val attempt = attempts.find { it.id == attemptId }
    val liveAttempt = container.quizAttemptRepository.getById(attemptId)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CseSurface)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }
            Text(
                text = "Detalle del intento",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            if (attempt == null) {
                Text("Intento no encontrado", color = CseDanger)
                return@Column
            }
            AttemptSummary(attempt)
            Spacer(modifier = Modifier.height(20.dp))
            if (liveAttempt != null) {
                Button(
                    onClick = { onOpenAnswers(attemptId) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ver respuestas (solo lectura)")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Disponible mientras la sesión conserve el intento en memoria.",
                    style = MaterialTheme.typography.bodySmall,
                    color = CseMuted
                )
            } else {
                Text(
                    text = "El detalle de respuestas ya no está en memoria. Solo se muestra el resumen persistido.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = CseMuted
                )
            }
        }
    }
}

@Composable
private fun AttemptSummary(attempt: AttemptStat) {
    Text(
        text = attempt.courseTitle,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = "${attempt.correctCount} / ${attempt.total}",
        style = MaterialTheme.typography.headlineSmall,
        color = if (attempt.passed) CseGreen else CseDanger,
        fontWeight = FontWeight.Bold
    )
    Text(
        text = if (attempt.passed) "Aprobado" else "Reprobado",
        color = if (attempt.passed) CseGreen else CseDanger
    )
    Spacer(modifier = Modifier.height(12.dp))
    Text("Duración: ${attempt.durationMinutes} min", color = CseMuted)
    Text(
        text = "Fecha: ${formatDate(attempt.timestampMillis)}",
        color = CseMuted
    )
}

private fun formatDate(millis: Long): String {
    val formatter = DateTimeFormatter.ofPattern("d MMM yyyy HH:mm", Locale("es", "PE"))
    return Instant.ofEpochMilli(millis)
        .atZone(ZoneId.systemDefault())
        .format(formatter)
}
