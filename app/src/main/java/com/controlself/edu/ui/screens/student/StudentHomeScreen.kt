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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.controlself.edu.di.LocalAppContainer
import com.controlself.edu.domain.model.Course
import com.controlself.edu.domain.model.StudentDashboardMock
import com.controlself.edu.domain.repository.ScreenTimeRepository
import com.controlself.edu.ui.screens.student.components.CoursesSection
import com.controlself.edu.ui.screens.student.components.ProgressSection
import com.controlself.edu.ui.screens.student.components.ScreenTimeCard
import com.controlself.edu.ui.screens.student.components.StreakCard
import com.controlself.edu.ui.screens.student.components.UsageAccessBanner
import com.controlself.edu.ui.theme.CseBlue
import com.controlself.edu.ui.theme.CseMuted
import com.controlself.edu.ui.theme.CseSurface
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
    val scope = rememberCoroutineScope()

    val minutes by screenTime.observeTodayMinutes()
        .collectAsStateWithLifecycle(initialValue = 0)
    val hasPermission by screenTime.observeUsagePermissionGranted()
        .collectAsStateWithLifecycle(initialValue = false)
    val dashboard = StudentDashboardMock(socialMinutes = minutes)

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                scope.launch { screenTime.refresh() }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

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
                UsageAccessBanner(
                    onOpenSettings = { usageGateway.openUsageAccessSettings() }
                )
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
                streakDays = dashboard.streakDays,
                badges = dashboard.badges
            )
            Spacer(modifier = Modifier.height(24.dp))
            ProgressSection(mock = dashboard)
        }
    }
}
