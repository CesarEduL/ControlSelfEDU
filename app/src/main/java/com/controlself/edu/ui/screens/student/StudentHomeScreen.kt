package com.controlself.edu.ui.screens.student

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.controlself.edu.di.LocalAppContainer
import com.controlself.edu.domain.model.Course
import com.controlself.edu.domain.model.motivation.AchievementBadge
import com.controlself.edu.domain.model.motivation.AchievementId
import com.controlself.edu.domain.model.motivation.MotivationSnapshot
import com.controlself.edu.domain.model.stats.CourseStatBar
import com.controlself.edu.domain.model.stats.ScorePoint
import com.controlself.edu.domain.model.stats.StudentStatsDashboard
import com.controlself.edu.domain.repository.ScreenTimeRepository
import com.controlself.edu.ui.components.PrimaryFlatButton
import com.controlself.edu.ui.components.SecondaryFlatButton
import com.controlself.edu.ui.screens.student.components.CoursesSection
import com.controlself.edu.ui.screens.student.components.ProgressSection
import com.controlself.edu.ui.screens.student.components.ScreenTimeCard
import com.controlself.edu.ui.screens.student.components.StreakCard
import com.controlself.edu.ui.screens.student.components.StudentBottomBar
import com.controlself.edu.ui.screens.student.components.StudentTab
import com.controlself.edu.ui.screens.student.components.UsageAccessBanner
import com.controlself.edu.ui.theme.ControlSelfEDUTheme
import com.controlself.edu.ui.theme.CseBackground
import com.controlself.edu.ui.theme.CseOnSurfaceVariant
import com.controlself.edu.ui.theme.CseOutlineVariant
import com.controlself.edu.ui.theme.CsePrimary
import com.controlself.edu.ui.theme.CsePrimaryContainer
import com.controlself.edu.ui.theme.CsePrimaryFixedDim
import com.controlself.edu.ui.theme.CseWhite
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun StudentHomeScreen(
    displayName: String,
    onCourseClick: (Course) -> Unit,
    onSimulateLock: () -> Unit,
    onClearLock: () -> Unit,
    onLogout: () -> Unit
) {
    val container = LocalAppContainer.current
    val screenTime = container.screenTimeRepository
    val usageGateway = container.usageStatsGateway
    val achievements = container.achievementRepository
    val lockRepository = container.lockRepository
    val scope = rememberCoroutineScope()

    val minutes by screenTime.observeTodayMinutes()
        .collectAsStateWithLifecycle(initialValue = 0)
    val hasPermission by screenTime.observeUsagePermissionGranted()
        .collectAsStateWithLifecycle(initialValue = false)
    val motivation by achievements.observeSnapshot()
        .collectAsStateWithLifecycle(initialValue = MotivationSnapshot())
    val stats by container.statsAggregator.observeDashboard()
        .collectAsStateWithLifecycle(initialValue = StudentStatsDashboard())

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner, screenTime, achievements, lockRepository) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                scope.launch {
                    screenTime.refresh()
                    achievements.syncResponsibleUsage(
                        minutesUsed = screenTime.observeTodayMinutes().first(),
                        isForceLocked = lockRepository.observeLocked().first()
                    )
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    StudentHomeContent(
        displayName = displayName,
        minutes = minutes,
        hasPermission = hasPermission,
        streakDays = motivation.streak.currentDays,
        badges = motivation.badges,
        stats = stats,
        onOpenSettings = { usageGateway.openUsageAccessSettings() },
        onCourseClick = onCourseClick,
        onSimulateLock = onSimulateLock,
        onClearLock = onClearLock,
        onLogout = onLogout
    )
}

@Composable
private fun StudentHomeContent(
    displayName: String,
    minutes: Int,
    hasPermission: Boolean,
    streakDays: Int,
    badges: List<AchievementBadge>,
    stats: StudentStatsDashboard,
    onOpenSettings: () -> Unit,
    onCourseClick: (Course) -> Unit,
    onSimulateLock: () -> Unit,
    onClearLock: () -> Unit,
    onLogout: () -> Unit
) {
    var tab by remember { mutableStateOf(StudentTab.HOME) }

    Scaffold(
        containerColor = CseBackground,
        bottomBar = {
            StudentBottomBar(
                selected = tab,
                onSelect = { tab = it }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            StudentTopBar(displayName = displayName)
            when (tab) {
                StudentTab.HOME -> HomeTab(
                    displayName = displayName,
                    minutes = minutes,
                    hasPermission = hasPermission,
                    onOpenSettings = onOpenSettings,
                    onSimulateLock = onSimulateLock,
                    onClearLock = onClearLock,
                    onSeeCourses = { tab = StudentTab.COURSES }
                )
                StudentTab.COURSES -> CoursesTab(onCourseClick = onCourseClick)
                StudentTab.STATS -> StatsTab(
                    streakDays = streakDays,
                    badges = badges,
                    stats = stats
                )
                StudentTab.PROFILE -> ProfileTab(
                    displayName = displayName,
                    streakDays = streakDays,
                    stats = stats,
                    onLogout = onLogout
                )
            }
        }
    }
}

@Composable
private fun StudentTopBar(displayName: String) {
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
                    .border(2.dp, CsePrimaryFixedDim, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = displayName.take(1).uppercase(),
                    style = MaterialTheme.typography.titleMedium,
                    color = CsePrimaryFixedDim,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        HorizontalDivider(color = CseOutlineVariant, thickness = 1.dp)
    }
}

@Composable
private fun HomeTab(
    displayName: String,
    minutes: Int,
    hasPermission: Boolean,
    onOpenSettings: () -> Unit,
    onSimulateLock: () -> Unit,
    onClearLock: () -> Unit,
    onSeeCourses: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp, bottom = 24.dp)
    ) {
        Text(
            text = "Hola, $displayName",
            style = MaterialTheme.typography.labelLarge,
            color = CseOnSurfaceVariant,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Tu aprendizaje de hoy",
            style = MaterialTheme.typography.headlineLarge,
            color = CsePrimary,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.height(20.dp))
        if (!hasPermission) {
            UsageAccessBanner(onOpenSettings = onOpenSettings)
            Spacer(modifier = Modifier.height(16.dp))
        }
        ScreenTimeCard(
            minutesUsed = minutes,
            limitMinutes = ScreenTimeRepository.DAILY_LIMIT_MINUTES
        )
        if (hasPermission) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Monitoreo activo (redes + juegos)",
                style = MaterialTheme.typography.bodySmall,
                color = CseOnSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        PrimaryFlatButton(
            text = "Simular bloqueo",
            onClick = onSimulateLock
        )
        Spacer(modifier = Modifier.height(12.dp))
        SecondaryFlatButton(
            text = "Quitar bloqueo (demo)",
            onClick = onClearLock
        )
        Spacer(modifier = Modifier.height(12.dp))
        SecondaryFlatButton(
            text = "Ver cursos",
            onClick = onSeeCourses
        )
    }
}

@Composable
private fun CoursesTab(onCourseClick: (Course) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp, bottom = 24.dp)
    ) {
        Text(
            text = "Cursos",
            style = MaterialTheme.typography.headlineLarge,
            color = CsePrimary,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Elige una materia para practicar.",
            style = MaterialTheme.typography.bodyMedium,
            color = CseOnSurfaceVariant
        )
        Spacer(modifier = Modifier.height(20.dp))
        CoursesSection(onCourseClick = onCourseClick)
    }
}

@Composable
private fun StatsTab(
    streakDays: Int,
    badges: List<AchievementBadge>,
    stats: StudentStatsDashboard
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp, bottom = 24.dp)
    ) {
        Text(
            text = "Estadísticas",
            style = MaterialTheme.typography.headlineLarge,
            color = CsePrimary,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Tu progreso y logros.",
            style = MaterialTheme.typography.bodyMedium,
            color = CseOnSurfaceVariant
        )
        Spacer(modifier = Modifier.height(20.dp))
        StreakCard(streakDays = streakDays, badges = badges)
        Spacer(modifier = Modifier.height(16.dp))
        ProgressSection(stats = stats)
    }
}

@Composable
private fun ProfileTab(
    displayName: String,
    streakDays: Int,
    stats: StudentStatsDashboard,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp, bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(88.dp)
                .clip(CircleShape)
                .background(CsePrimaryContainer)
                .border(2.dp, CsePrimaryFixedDim, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = displayName.take(1).uppercase(),
                style = MaterialTheme.typography.displayMedium,
                color = CsePrimaryFixedDim,
                fontWeight = FontWeight.ExtraBold
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = displayName,
            style = MaterialTheme.typography.headlineMedium,
            color = CsePrimary,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Estudiante",
            style = MaterialTheme.typography.bodyMedium,
            color = CseOnSurfaceVariant
        )
        Spacer(modifier = Modifier.height(24.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(CseWhite)
                .border(1.dp, CseOutlineVariant, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            ProfileMetric("Racha actual", "$streakDays días")
            Spacer(modifier = Modifier.height(10.dp))
            ProfileMetric(
                "Promedio",
                stats.averageScore?.let { String.format("%.1f / 20", it) } ?: "—"
            )
            Spacer(modifier = Modifier.height(10.dp))
            ProfileMetric("Evaluaciones", stats.evaluationsCount.toString())
            Spacer(modifier = Modifier.height(10.dp))
            ProfileMetric(
                "Logros",
                "${stats.achievementsUnlocked} / ${stats.achievementsTotal}"
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        SecondaryFlatButton(text = "Cerrar sesión", onClick = onLogout)
    }
}

@Composable
private fun ProfileMetric(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = CseOnSurfaceVariant,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = value,
            color = CsePrimary,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun StudentHomeScreenPreview() {
    ControlSelfEDUTheme {
        StudentHomeContent(
            displayName = "César",
            minutes = 18,
            hasPermission = false,
            streakDays = 3,
            badges = AchievementId.ALL.mapIndexed { index, id ->
                AchievementBadge(
                    id = id,
                    title = id.title,
                    description = id.description,
                    unlocked = index < 2,
                    unlockedAtMillis = if (index < 2) System.currentTimeMillis() else null
                )
            },
            stats = StudentStatsDashboard(
                evaluationsCount = 4,
                averageScore = 16.5,
                studyMinutesTotal = 42,
                entertainmentMinutesToday = 18,
                coursesPassedDistinct = 2,
                achievementsUnlocked = 2,
                achievementsTotal = 5,
                currentStreak = 3,
                maxStreak = 5,
                recentScores = listOf(
                    ScorePoint("1", 14),
                    ScorePoint("2", 16),
                    ScorePoint("3", 18)
                ),
                courseBars = listOf(
                    CourseStatBar("math", "Mate", 2, 3),
                    CourseStatBar("comms", "Comms", 1, 1),
                    CourseStatBar("science", "CyT", 0, 0)
                ),
                hasAttemptData = true
            ),
            onOpenSettings = {},
            onCourseClick = {},
            onSimulateLock = {},
            onClearLock = {},
            onLogout = {}
        )
    }
}
