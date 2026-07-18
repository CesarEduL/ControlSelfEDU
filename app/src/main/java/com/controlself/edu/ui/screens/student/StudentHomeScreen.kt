package com.controlself.edu.ui.screens.student

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.controlself.edu.ui.screens.student.components.CoursesSection
import com.controlself.edu.ui.screens.student.components.ProgressSection
import com.controlself.edu.ui.screens.student.components.ScreenTimeCard
import com.controlself.edu.ui.screens.student.components.StreakCard
import com.controlself.edu.ui.screens.student.components.UsageAccessBanner
import com.controlself.edu.ui.theme.ControlSelfEDUTheme
import com.controlself.edu.ui.theme.CseBlue
import com.controlself.edu.ui.theme.CseMuted
import com.controlself.edu.ui.theme.CseSurface
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun StudentHomeScreen(
    displayName: String,
    onCourseClick: (Course) -> Unit,
    onSimulateLock: () -> Unit,
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
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CseSurface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onLogout) {
                Text("Cerrar sesión")
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
        ) {
            Text(
                text = "Bienvenido a ControlSelf EDU",
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
                    color = CseMuted
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(
                onClick = onSimulateLock,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Simular tiempo agotado (QA)")
            }
            Spacer(modifier = Modifier.height(24.dp))
            CoursesSection(onCourseClick = onCourseClick)
            Spacer(modifier = Modifier.height(24.dp))
            StreakCard(
                streakDays = streakDays,
                badges = badges
            )
            Spacer(modifier = Modifier.height(24.dp))
            ProgressSection(stats = stats)
        }
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
            onLogout = {}
        )
    }
}
