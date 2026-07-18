package com.controlself.edu.ui.screens.parent

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.controlself.edu.di.LocalAppContainer
import com.controlself.edu.domain.model.parent.DayUsagePoint
import com.controlself.edu.domain.model.parent.ParentDashboard
import com.controlself.edu.domain.model.stats.AttemptStat
import com.controlself.edu.ui.theme.CseBlue
import com.controlself.edu.ui.theme.CseGreen
import com.controlself.edu.ui.theme.CseMuted
import com.controlself.edu.ui.theme.CseSurface
import com.controlself.edu.ui.theme.CseTeal
import com.controlself.edu.ui.theme.CseWarning
import com.controlself.edu.ui.theme.CseWhite
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun ParentHomeScreen(
    displayName: String,
    onOpenAttempt: (attemptId: String) -> Unit,
    onLogout: () -> Unit
) {
    val container = LocalAppContainer.current
    val scope = rememberCoroutineScope()
    val dashboard by container.parentDashboardAggregator.observeDashboard()
        .collectAsStateWithLifecycle(initialValue = ParentDashboard())

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                scope.launch { container.screenTimeRepository.refresh() }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    ParentHomeContent(
        displayName = displayName,
        dashboard = dashboard,
        onOpenAttempt = onOpenAttempt,
        onLogout = onLogout
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ParentHomeContent(
    displayName: String,
    dashboard: ParentDashboard,
    onOpenAttempt: (attemptId: String) -> Unit,
    onLogout: () -> Unit
) {
    val stats = dashboard.stats
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CseSurface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = onLogout) { Text("Cerrar sesión") }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
        ) {
            Text(
                text = "Panel padre / madre",
                style = MaterialTheme.typography.headlineMedium,
                color = CseBlue,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Hola, $displayName",
                style = MaterialTheme.typography.titleMedium,
                color = CseMuted
            )
            Spacer(modifier = Modifier.height(16.dp))

            InfoCard(title = "Hijo vinculado") {
                Text(
                    text = dashboard.child.displayName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Código: ${dashboard.child.linkCode}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = CseMuted
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            InfoCard(title = "Tiempo en pantalla") {
                MetricLine("Hoy (redes/juegos)", "${stats.entertainmentMinutesToday} min")
                MetricLine("Últimos 7 días", "${dashboard.entertainmentWeekTotal} min")
                MetricLine("Estudio acumulado", "${stats.studyMinutesTotal} min", last = true)
            }

            Spacer(modifier = Modifier.height(12.dp))
            InfoCard(title = "Progreso académico") {
                val avg = stats.averageScore?.let {
                    String.format(Locale.getDefault(), "%.1f / 20", it)
                } ?: "—"
                MetricLine("Promedio", avg)
                MetricLine("Evaluaciones", stats.evaluationsCount.toString())
                MetricLine(
                    "Racha",
                    "${stats.currentStreak} días (máx. ${stats.maxStreak})",
                    last = true
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            InfoCard(title = "Cursos desarrollados") {
                if (dashboard.courseBars.all { it.attemptCount == 0 }) {
                    Text("Aún sin lecciones", color = CseMuted)
                } else {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        dashboard.courseBars.filter { it.attemptCount > 0 }.forEach { bar ->
                            AssistChip(
                                onClick = {},
                                label = {
                                    Text("${bar.label}: ${bar.passedCount} aprob. / ${bar.attemptCount}")
                                },
                                colors = AssistChipDefaults.assistChipColors(
                                    containerColor = CseBlue.copy(alpha = 0.12f),
                                    labelColor = CseBlue
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            InfoCard(title = "Logros obtenidos") {
                val unlocked = dashboard.badges.filter { it.unlocked }
                if (unlocked.isEmpty()) {
                    Text("Sin logros todavía", color = CseMuted)
                } else {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        unlocked.forEach { badge ->
                            AssistChip(
                                onClick = {},
                                label = { Text(badge.title) },
                                colors = AssistChipDefaults.assistChipColors(
                                    containerColor = CseGreen.copy(alpha = 0.15f),
                                    labelColor = CseGreen
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            InfoCard(title = "Evolución semanal") {
                WeeklyChart(points = dashboard.weekly)
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    LegendDot(CseTeal, "Estudio")
                    LegendDot(CseWarning, "Redes")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            InfoCard(title = "Intentos recientes") {
                if (dashboard.recentAttempts.isEmpty()) {
                    Text("Aún no hay evaluaciones", color = CseMuted)
                } else {
                    dashboard.recentAttempts.forEach { attempt ->
                        AttemptRow(attempt = attempt, onClick = { onOpenAttempt(attempt.id) })
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                color = CseWhite,
                shadowElevation = 1.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Protección del dispositivo",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "La contraseña de administrador anti-desinstalación se configura en PRP-13. " +
                            "No se muestra en pantallas del estudiante.",
                        style = MaterialTheme.typography.bodySmall,
                        color = CseMuted
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoCard(title: String, content: @Composable () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = CseWhite,
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(10.dp))
            content()
        }
    }
}

@Composable
private fun MetricLine(label: String, value: String, last: Boolean = false) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(label, color = CseMuted, modifier = Modifier.weight(1f))
        Text(value, fontWeight = FontWeight.SemiBold)
    }
    if (!last) Spacer(modifier = Modifier.height(6.dp))
}

@Composable
private fun AttemptRow(attempt: AttemptStat, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = attempt.courseTitle,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = "${attempt.correctCount}/${attempt.total}" +
                (if (attempt.passed) " · Aprobado" else " · Reprobado") +
                " · ${formatAttemptDate(attempt.timestampMillis)}",
            style = MaterialTheme.typography.bodySmall,
            color = CseMuted
        )
    }
}

@Composable
private fun WeeklyChart(points: List<DayUsagePoint>) {
    val maxValue = points
        .flatMap { listOf(it.studyMinutes, it.entertainmentMinutes) }
        .maxOrNull()
        ?.coerceAtLeast(1)
        ?.toFloat() ?: 1f

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
        ) {
            Canvas(modifier = Modifier.matchParentSize()) {
                val groups = points.size.coerceAtLeast(1)
                val groupGap = size.width * 0.04f
                val groupWidth = (size.width - groupGap * (groups + 1)) / groups
                val barGap = groupWidth * 0.1f
                val barWidth = (groupWidth - barGap) / 2f

                points.forEachIndexed { index, point ->
                    val left = groupGap + index * (groupWidth + groupGap)
                    val studyH = size.height * (point.studyMinutes / maxValue)
                    val entH = size.height * (point.entertainmentMinutes / maxValue)
                    drawRoundRect(
                        color = CseTeal,
                        topLeft = Offset(left, size.height - studyH),
                        size = Size(barWidth, studyH.coerceAtLeast(2f)),
                        cornerRadius = CornerRadius(6f, 6f)
                    )
                    drawRoundRect(
                        color = CseWarning,
                        topLeft = Offset(left + barWidth + barGap, size.height - entH),
                        size = Size(barWidth, entH.coerceAtLeast(2f)),
                        cornerRadius = CornerRadius(6f, 6f)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            points.forEach { point ->
                Text(
                    text = point.label.take(3),
                    style = MaterialTheme.typography.labelSmall,
                    color = CseMuted,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun LegendDot(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = "● ", color = color, fontWeight = FontWeight.Bold)
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = CseMuted)
    }
}

private fun formatAttemptDate(millis: Long): String {
    val formatter = DateTimeFormatter.ofPattern("d MMM HH:mm", Locale("es", "PE"))
    return Instant.ofEpochMilli(millis)
        .atZone(ZoneId.systemDefault())
        .format(formatter)
}
