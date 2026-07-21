package com.controlself.edu.ui.screens.parent

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.Forum
import androidx.compose.material.icons.outlined.Science
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.controlself.edu.domain.model.stats.CourseStatBar
import com.controlself.edu.domain.model.quiz.QuizAttempt
import com.controlself.edu.domain.repository.ScreenTimeRepository
import com.controlself.edu.ui.components.PrimaryFlatButton
import com.controlself.edu.ui.components.SecondaryFlatButton
import com.controlself.edu.ui.screens.parent.components.ParentBottomBar
import com.controlself.edu.ui.screens.parent.components.ParentTab
import com.controlself.edu.ui.theme.CseBackground
import com.controlself.edu.ui.theme.CseError
import com.controlself.edu.ui.theme.CseErrorContainer
import com.controlself.edu.ui.theme.CseOnSecondaryContainer
import com.controlself.edu.ui.theme.CseOnSurfaceVariant
import com.controlself.edu.ui.theme.CseOutline
import com.controlself.edu.ui.theme.CseOutlineVariant
import com.controlself.edu.ui.theme.CsePrimary
import com.controlself.edu.ui.theme.CsePrimaryContainer
import com.controlself.edu.ui.theme.CsePrimaryFixedDim
import com.controlself.edu.ui.theme.CseSecondary
import com.controlself.edu.ui.theme.CseSecondaryContainer
import com.controlself.edu.ui.theme.CseSurfaceContainer
import com.controlself.edu.ui.theme.CseSurfaceLow
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
    onOpenProtection: () -> Unit,
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
        onOpenProtection = onOpenProtection,
        onLogout = onLogout
    )
}

@Composable
private fun ParentHomeContent(
    displayName: String,
    dashboard: ParentDashboard,
    onOpenAttempt: (attemptId: String) -> Unit,
    onOpenProtection: () -> Unit,
    onLogout: () -> Unit
) {
    var tab by remember { mutableStateOf(ParentTab.HOME) }

    Scaffold(
        containerColor = CseBackground,
        bottomBar = {
            ParentBottomBar(selected = tab, onSelect = { tab = it })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            ParentTopBar(displayName = displayName)
            when (tab) {
                ParentTab.HOME -> HomeOverview(
                    dashboard = dashboard,
                    onOpenProtection = onOpenProtection,
                    onOpenAttempt = onOpenAttempt
                )
                ParentTab.COURSES -> CoursesOverview(dashboard = dashboard)
                ParentTab.STATS -> StatsOverview(dashboard = dashboard)
                ParentTab.PROFILE -> ProfileOverview(
                    displayName = displayName,
                    dashboard = dashboard,
                    onOpenProtection = onOpenProtection,
                    onLogout = onLogout
                )
            }
        }
    }
}

@Composable
private fun ParentTopBar(displayName: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(CseBackground)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "ControlSelf EDU",
                style = MaterialTheme.typography.titleLarge,
                color = CsePrimary,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.weight(1f)
            )
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(CsePrimaryContainer)
                    .border(1.dp, CseOutlineVariant, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = displayName.take(1).uppercase(),
                    color = CsePrimaryFixedDim,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        HorizontalDivider(color = CseOutlineVariant, thickness = 1.dp)
    }
}

@Composable
private fun HomeOverview(
    dashboard: ParentDashboard,
    onOpenProtection: () -> Unit,
    onOpenAttempt: (attemptId: String) -> Unit
) {
    val stats = dashboard.stats
    val limit = ScreenTimeRepository.DAILY_LIMIT_MINUTES
    val today = stats.entertainmentMinutesToday
    val progress = (today.toFloat() / limit).coerceIn(0f, 1f)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Panel padre",
            style = MaterialTheme.typography.headlineLarge,
            color = CsePrimary,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Hijo activo",
                style = MaterialTheme.typography.labelSmall,
                color = CseOnSecondaryContainer,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(CseSecondaryContainer)
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = dashboard.child.linkCode,
                style = MaterialTheme.typography.labelLarge,
                color = CsePrimary,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        PrimaryFlatButton(
            text = "Configurar protección",
            onClick = onOpenProtection
        )

        Spacer(modifier = Modifier.height(16.dp))
        ScreenTimeStatusCard(
            todayMinutes = today,
            limitMinutes = limit,
            weekMinutes = dashboard.entertainmentWeekTotal,
            progress = progress
        )

        Spacer(modifier = Modifier.height(12.dp))
        AcademicProgressCard(courseBars = dashboard.courseBars)

        Spacer(modifier = Modifier.height(12.dp))
        RecentAttemptsCard(
            attempts = dashboard.recentAttempts,
            onOpenAttempt = onOpenAttempt
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CoursesOverview(dashboard: ParentDashboard) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Cursos",
            style = MaterialTheme.typography.headlineLarge,
            color = CsePrimary,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Progreso por materia de ${dashboard.child.displayName}.",
            color = CseOnSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        AcademicProgressCard(courseBars = dashboard.courseBars)
        Spacer(modifier = Modifier.height(12.dp))
        FlatSectionCard(title = "Logros") {
            val unlocked = dashboard.badges.filter { it.unlocked }
            if (unlocked.isEmpty()) {
                Text("Sin logros todavía", color = CseOnSurfaceVariant)
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
                                containerColor = CseSecondaryContainer,
                                labelColor = CseSecondary
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatsOverview(dashboard: ParentDashboard) {
    val stats = dashboard.stats
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Estadísticas",
            style = MaterialTheme.typography.headlineLarge,
            color = CsePrimary,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.height(16.dp))
        FlatSectionCard(title = "Resumen académico") {
            MetricLine(
                "Promedio",
                stats.averageScore?.let {
                    String.format(
                        Locale.getDefault(),
                        "%.1f / %d",
                        it,
                        QuizAttempt.TOTAL_QUESTIONS
                    )
                } ?: "—"
            )
            MetricLine("Evaluaciones", stats.evaluationsCount.toString())
            MetricLine(
                "Racha",
                "${stats.currentStreak} días (máx. ${stats.maxStreak})",
                last = true
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        FlatSectionCard(title = "Estudio vs redes") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                LegendDot(CseSecondary, "Estudio")
                LegendDot(CseOutline, "Redes")
            }
            Spacer(modifier = Modifier.height(12.dp))
            WeeklyChart(points = dashboard.weekly)
        }
    }
}

@Composable
private fun ProfileOverview(
    displayName: String,
    dashboard: ParentDashboard,
    onOpenProtection: () -> Unit,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(88.dp)
                .clip(CircleShape)
                .background(CsePrimaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = displayName.take(1).uppercase(),
                style = MaterialTheme.typography.displayMedium,
                color = CsePrimaryFixedDim,
                fontWeight = FontWeight.ExtraBold
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = displayName,
            style = MaterialTheme.typography.headlineMedium,
            color = CsePrimary,
            fontWeight = FontWeight.Bold
        )
        Text("Padre / madre", color = CseOnSurfaceVariant)
        Spacer(modifier = Modifier.height(20.dp))
        FlatSectionCard(title = "Hijo vinculado") {
            Text(dashboard.child.displayName, fontWeight = FontWeight.SemiBold, color = CsePrimary)
            Text("Código: ${dashboard.child.linkCode}", color = CseOnSurfaceVariant)
        }
        Spacer(modifier = Modifier.height(12.dp))
        PrimaryFlatButton(text = "Protección del dispositivo", onClick = onOpenProtection)
        Spacer(modifier = Modifier.height(10.dp))
        SecondaryFlatButton(text = "Cerrar sesión", onClick = onLogout)
    }
}

@Composable
private fun ScreenTimeStatusCard(
    todayMinutes: Int,
    limitMinutes: Int,
    weekMinutes: Int,
    progress: Float
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = CseWhite,
        border = BorderStroke(1.dp, CseOutlineVariant),
        shadowElevation = 0.dp
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(CseSecondary)
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "ESTADO",
                        style = MaterialTheme.typography.labelSmall,
                        color = CseOnSurfaceVariant,
                        fontWeight = FontWeight.Bold
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(CseSecondary)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Activo",
                            style = MaterialTheme.typography.labelSmall,
                            color = CseSecondary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(CseSecondaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Filled.Schedule,
                            contentDescription = null,
                            tint = CseOnSecondaryContainer
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Tiempo en pantalla hoy",
                            style = MaterialTheme.typography.labelSmall,
                            color = CseOnSurfaceVariant
                        )
                        Text(
                            text = "${formatMinutes(todayMinutes)} / ${formatMinutes(limitMinutes)}",
                            style = MaterialTheme.typography.headlineSmall,
                            color = CsePrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                    color = CseSecondary,
                    trackColor = CseSurfaceContainer,
                    strokeCap = StrokeCap.Round
                )
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = CseOutlineVariant)
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        "Total semana",
                        color = CseOnSurfaceVariant,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        formatMinutes(weekMinutes),
                        color = CsePrimary,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}

@Composable
private fun AcademicProgressCard(courseBars: List<CourseStatBar>) {
    FlatSectionCard(title = "Progreso académico") {
        if (courseBars.all { it.attemptCount == 0 }) {
            Text("Aún sin lecciones", color = CseOnSurfaceVariant)
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                courseBars.forEach { bar ->
                    CourseProgressTile(bar = bar)
                }
            }
        }
    }
}

@Composable
private fun CourseProgressTile(bar: CourseStatBar) {
    val icon: ImageVector = when (bar.courseId) {
        "math" -> Icons.Outlined.Calculate
        "comms" -> Icons.Outlined.Forum
        else -> Icons.Outlined.Science
    }
    val pct = if (bar.attemptCount == 0) {
        0
    } else {
        ((bar.passedCount * 100f) / bar.attemptCount).toInt()
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(CseSurfaceLow)
            .border(1.dp, CseOutlineVariant, RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(CseSecondaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = CseOnSecondaryContainer)
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(bar.label, fontWeight = FontWeight.Bold, color = CsePrimary)
            Text(
                "${bar.passedCount} aprob. / ${bar.attemptCount} intentos",
                style = MaterialTheme.typography.labelSmall,
                color = CseOnSurfaceVariant
            )
        }
        Text(
            "$pct%",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold,
            color = CsePrimary
        )
    }
}

@Composable
private fun RecentAttemptsCard(
    attempts: List<AttemptStat>,
    onOpenAttempt: (attemptId: String) -> Unit
) {
    FlatSectionCard(title = "Intentos recientes") {
        if (attempts.isEmpty()) {
            Text("Aún no hay evaluaciones", color = CseOnSurfaceVariant)
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                attempts.take(5).forEach { attempt ->
                    AttemptRow(attempt = attempt, onClick = { onOpenAttempt(attempt.id) })
                }
            }
        }
    }
}

@Composable
private fun FlatSectionCard(title: String, content: @Composable () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = CseWhite,
        border = BorderStroke(1.dp, CseOutlineVariant),
        shadowElevation = 0.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = CsePrimary
            )
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
private fun MetricLine(label: String, value: String, last: Boolean = false) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(label, color = CseOnSurfaceVariant, modifier = Modifier.weight(1f))
        Text(value, fontWeight = FontWeight.SemiBold, color = CsePrimary)
    }
    if (!last) Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun AttemptRow(attempt: AttemptStat, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(CseSurfaceLow)
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(if (attempt.passed) CseSecondaryContainer else CseErrorContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (attempt.passed) Icons.Filled.CheckCircle else Icons.Filled.Error,
                contentDescription = null,
                tint = if (attempt.passed) CseSecondary else CseError,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = attempt.courseTitle,
                fontWeight = FontWeight.Bold,
                color = CsePrimary,
                style = MaterialTheme.typography.labelLarge
            )
            Text(
                text = formatAttemptDate(attempt.timestampMillis).uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = CseOnSurfaceVariant
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "${attempt.correctCount}/${attempt.total}",
                fontWeight = FontWeight.Bold,
                color = CsePrimary
            )
            Text(
                text = if (attempt.passed) "APROBADO" else "REINTENTAR",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = if (attempt.passed) CseSecondary else CseError
            )
        }
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
                        color = CseSecondary,
                        topLeft = Offset(left, size.height - studyH),
                        size = Size(barWidth, studyH.coerceAtLeast(2f)),
                        cornerRadius = CornerRadius(6f, 6f)
                    )
                    drawRoundRect(
                        color = CseOutline,
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
                    color = CseOnSurfaceVariant,
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
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(label, style = MaterialTheme.typography.labelSmall, color = CseOnSurfaceVariant)
    }
}

private fun formatMinutes(minutes: Int): String {
    if (minutes < 60) return "$minutes min"
    val h = minutes / 60
    val m = minutes % 60
    return if (m == 0) "${h}h" else "${h}h ${m}m"
}

private fun formatAttemptDate(millis: Long): String {
    val formatter = DateTimeFormatter.ofPattern("d MMM HH:mm", Locale("es", "PE"))
    return Instant.ofEpochMilli(millis)
        .atZone(ZoneId.systemDefault())
        .format(formatter)
}
