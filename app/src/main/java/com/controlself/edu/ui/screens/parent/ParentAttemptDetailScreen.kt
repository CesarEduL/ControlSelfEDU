package com.controlself.edu.ui.screens.parent

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.controlself.edu.di.LocalAppContainer
import com.controlself.edu.domain.model.stats.AttemptStat
import com.controlself.edu.ui.components.PrimaryFlatButton
import com.controlself.edu.ui.theme.CseBackground
import com.controlself.edu.ui.theme.CseError
import com.controlself.edu.ui.theme.CseOnSecondaryContainer
import com.controlself.edu.ui.theme.CseOnSurfaceVariant
import com.controlself.edu.ui.theme.CseOutlineVariant
import com.controlself.edu.ui.theme.CsePrimary
import com.controlself.edu.ui.theme.CseSecondary
import com.controlself.edu.ui.theme.CseSecondaryContainer
import com.controlself.edu.ui.theme.CseSurfaceContainer
import com.controlself.edu.ui.theme.CseSurfaceLow
import com.controlself.edu.ui.theme.CseWhite
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.min

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
            .background(CseBackground)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = CsePrimary
                )
            }
            Text(
                text = "Detalle del intento",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = CsePrimary
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(bottom = 32.dp)
        ) {
            if (attempt == null) {
                Text("Intento no encontrado", color = CseError)
                return@Column
            }

            Text(
                text = if (attempt.passed) "Evaluación completada" else "Requiere reintento",
                style = MaterialTheme.typography.labelSmall,
                color = if (attempt.passed) CseOnSecondaryContainer else CseError,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(
                        if (attempt.passed) CseSecondaryContainer else CseError.copy(alpha = 0.12f)
                    )
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = attempt.courseTitle,
                style = MaterialTheme.typography.headlineLarge,
                color = CsePrimary,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = formatDate(attempt.timestampMillis),
                style = MaterialTheme.typography.bodyMedium,
                color = CseOnSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))
            ScoreRingCard(attempt = attempt)

            Spacer(modifier = Modifier.height(12.dp))
            MetricsGrid(attempt = attempt)

            Spacer(modifier = Modifier.height(20.dp))
            if (liveAttempt != null) {
                PrimaryFlatButton(
                    text = "Ver respuestas",
                    onClick = { onOpenAnswers(attemptId) }
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Revisión solo lectura mientras el intento esté en memoria.",
                    style = MaterialTheme.typography.bodySmall,
                    color = CseOnSurfaceVariant
                )
            } else {
                Text(
                    text = "El detalle de respuestas ya no está en memoria. Solo se muestra el resumen persistido.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = CseOnSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun ScoreRingCard(attempt: AttemptStat) {
    val ratio = if (attempt.total == 0) 0f else attempt.correctCount.toFloat() / attempt.total
    val pct = (ratio * 100).toInt()
    val ringColor = if (attempt.passed) CseSecondary else CseError

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CseWhite)
            .border(1.dp, CseOutlineVariant, RoundedCornerShape(16.dp))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "PUNTAJE FINAL",
            style = MaterialTheme.typography.labelSmall,
            color = CseOnSurfaceVariant,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier.size(140.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val stroke = 10.dp.toPx()
                val diameter = size.minDimension - stroke
                val topLeft = Offset((size.width - diameter) / 2f, (size.height - diameter) / 2f)
                drawArc(
                    color = CseSurfaceContainer,
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    topLeft = topLeft,
                    size = Size(diameter, diameter),
                    style = Stroke(width = stroke, cap = StrokeCap.Round)
                )
                drawArc(
                    color = ringColor,
                    startAngle = -90f,
                    sweepAngle = 360f * ratio,
                    useCenter = false,
                    topLeft = topLeft,
                    size = Size(diameter, diameter),
                    style = Stroke(width = stroke, cap = StrokeCap.Round)
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "${attempt.correctCount}/${attempt.total}",
                    style = MaterialTheme.typography.headlineLarge,
                    color = CsePrimary,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = "$pct%",
                    style = MaterialTheme.typography.labelMedium,
                    color = CseOnSurfaceVariant
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = if (attempt.passed) {
                "Buen desempeño. Dominio de los conceptos principales."
            } else {
                "No alcanzó el mínimo. Revisa las respuestas e inténtalo de nuevo."
            },
            style = MaterialTheme.typography.bodyMedium,
            color = CseOnSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun MetricsGrid(attempt: AttemptStat) {
    val wrong = attempt.total - attempt.correctCount
    val secsPerQ = if (attempt.total == 0) {
        0
    } else {
        min(999, (attempt.durationMinutes * 60) / attempt.total.coerceAtLeast(1))
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CseWhite)
            .border(1.dp, CseOutlineVariant, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MetricTile(
                icon = { Icon(Icons.Outlined.Schedule, null, tint = CsePrimary) },
                label = "Duración",
                value = "${attempt.durationMinutes} min",
                modifier = Modifier.weight(1f)
            )
            MetricTile(
                icon = { Icon(Icons.Outlined.Speed, null, tint = CsePrimary) },
                label = "Ritmo",
                value = "${secsPerQ}s / p",
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MetricTile(
                icon = {
                    Text("✓", color = CseSecondary, fontWeight = FontWeight.Bold)
                },
                label = "Correctas",
                value = attempt.correctCount.toString(),
                modifier = Modifier.weight(1f)
            )
            MetricTile(
                icon = {
                    Text("✗", color = CseError, fontWeight = FontWeight.Bold)
                },
                label = "Incorrectas",
                value = wrong.toString(),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun MetricTile(
    icon: @Composable () -> Unit,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(CseSurfaceLow)
            .border(1.dp, CseOutlineVariant, RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        icon()
        Spacer(modifier = Modifier.height(8.dp))
        Text(label, style = MaterialTheme.typography.labelSmall, color = CseOnSurfaceVariant)
        Text(value, fontWeight = FontWeight.Bold, color = CsePrimary)
    }
}

private fun formatDate(millis: Long): String {
    val formatter = DateTimeFormatter.ofPattern("d MMM yyyy · HH:mm", Locale("es", "PE"))
    return Instant.ofEpochMilli(millis)
        .atZone(ZoneId.systemDefault())
        .format(formatter)
}
