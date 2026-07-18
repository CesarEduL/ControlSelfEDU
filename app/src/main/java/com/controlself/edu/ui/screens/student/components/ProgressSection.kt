package com.controlself.edu.ui.screens.student.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.controlself.edu.domain.model.stats.CourseStatBar
import com.controlself.edu.domain.model.stats.ScorePoint
import com.controlself.edu.domain.model.stats.StudentStatsDashboard
import com.controlself.edu.ui.theme.CseBlue
import com.controlself.edu.ui.theme.CseMuted
import com.controlself.edu.ui.theme.CseTeal
import com.controlself.edu.ui.theme.CseWarning
import com.controlself.edu.ui.theme.CseWhite
import java.util.Locale

@Composable
fun ProgressSection(
    stats: StudentStatsDashboard,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = CseWhite,
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Mi progreso",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(12.dp))

            if (!stats.hasAttemptData) {
                EmptyStatsState()
            } else {
                val avgText = stats.averageScore?.let {
                    String.format(Locale.getDefault(), "%.1f / 20", it)
                } ?: "—"
                ProgressRow("Promedio de notas", avgText)
                ProgressRow("Evaluaciones", stats.evaluationsCount.toString())
                ProgressRow("Tiempo estudiado", "${stats.studyMinutesTotal} min")
                ProgressRow("Tiempo en redes (hoy)", "${stats.entertainmentMinutesToday} min")
                ProgressRow(
                    "Cursos con aprobación",
                    stats.coursesPassedDistinct.toString()
                )
                ProgressRow(
                    "Logros",
                    "${stats.achievementsUnlocked} / ${stats.achievementsTotal}"
                )
                ProgressRow(
                    "Racha (actual / máxima)",
                    "${stats.currentStreak} / ${stats.maxStreak}",
                    showDivider = false
                )

                Spacer(modifier = Modifier.height(20.dp))
                ChartCard(title = "Notas recientes") {
                    ScoreBarsChart(points = stats.recentScores)
                }
                Spacer(modifier = Modifier.height(16.dp))
                ChartCard(title = "Estudio vs redes (hoy)") {
                    StudyVsEntertainmentChart(
                        studyMinutes = stats.studyMinutesTotal,
                        entertainmentMinutes = stats.entertainmentMinutesToday
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                ChartCard(title = "Aprobadas por curso") {
                    CourseBarsChart(bars = stats.courseBars)
                }
            }
        }
    }
}

@Composable
private fun EmptyStatsState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Aún no hay datos",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = CseMuted
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Completa una evaluación para ver tus estadísticas y gráficos.",
            style = MaterialTheme.typography.bodyMedium,
            color = CseMuted,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ChartCard(
    title: String,
    content: @Composable () -> Unit
) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        color = CseMuted
    )
    Spacer(modifier = Modifier.height(8.dp))
    content()
}

@Composable
private fun ScoreBarsChart(points: List<ScorePoint>) {
    if (points.isEmpty()) {
        Text("Sin notas todavía", color = CseMuted, style = MaterialTheme.typography.bodySmall)
        return
    }
    val maxY = (points.maxOf { it.maxScore }).coerceAtLeast(1).toFloat()
    Column {
        SimpleVerticalBars(
            values = points.map { it.score.toFloat() },
            maxValue = maxY,
            barColor = CseBlue,
            labels = points.map { it.label }
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Puntaje sobre 20",
            style = MaterialTheme.typography.bodySmall,
            color = CseMuted
        )
    }
}

@Composable
private fun StudyVsEntertainmentChart(studyMinutes: Int, entertainmentMinutes: Int) {
    val maxValue = maxOf(studyMinutes, entertainmentMinutes, 1).toFloat()
    SimpleVerticalBars(
        values = listOf(studyMinutes.toFloat(), entertainmentMinutes.toFloat()),
        maxValue = maxValue,
        barColor = CseTeal,
        colors = listOf(CseTeal, CseWarning),
        labels = listOf("Estudio", "Redes")
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
        text = "Estudio acumulado · redes solo hoy",
        style = MaterialTheme.typography.bodySmall,
        color = CseMuted
    )
}

@Composable
private fun CourseBarsChart(bars: List<CourseStatBar>) {
    val maxValue = bars.maxOf { it.passedCount }.coerceAtLeast(1).toFloat()
    SimpleVerticalBars(
        values = bars.map { it.passedCount.toFloat() },
        maxValue = maxValue,
        barColor = CseTeal,
        labels = bars.map { it.label }
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
        text = bars.joinToString(" · ") { "${it.label}: ${it.passedCount}/${it.attemptCount}" },
        style = MaterialTheme.typography.bodySmall,
        color = CseMuted
    )
}

@Composable
private fun SimpleVerticalBars(
    values: List<Float>,
    maxValue: Float,
    barColor: Color,
    labels: List<String>,
    colors: List<Color>? = null
) {
    val chartHeight = 120.dp
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(chartHeight)
        ) {
            Canvas(modifier = Modifier.matchParentSize()) {
                val barCount = values.size.coerceAtLeast(1)
                val gap = size.width * 0.08f / barCount
                val barWidth = (size.width - gap * (barCount + 1)) / barCount
                values.forEachIndexed { index, value ->
                    val ratio = (value / maxValue).coerceIn(0f, 1f)
                    val barH = size.height * ratio
                    val left = gap + index * (barWidth + gap)
                    val top = size.height - barH
                    val color = colors?.getOrNull(index) ?: barColor
                    drawRoundRect(
                        color = color,
                        topLeft = Offset(left, top),
                        size = Size(barWidth, barH.coerceAtLeast(2f)),
                        cornerRadius = CornerRadius(8f, 8f)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            labels.forEach { label ->
                Text(
                    text = label,
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
private fun ProgressRow(
    label: String,
    value: String,
    showDivider: Boolean = true
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = CseMuted,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
    if (showDivider) {
        androidx.compose.material3.HorizontalDivider(color = CseMuted.copy(alpha = 0.15f))
    }
}
