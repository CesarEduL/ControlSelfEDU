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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.controlself.edu.di.LocalAppContainer
import com.controlself.edu.domain.model.Course
import com.controlself.edu.domain.model.StudentDashboardMock
import com.controlself.edu.domain.repository.ScreenTimeRepository
import com.controlself.edu.ui.screens.student.components.CoursesSection
import com.controlself.edu.ui.screens.student.components.ProgressSection
import com.controlself.edu.ui.screens.student.components.ScreenTimeCard
import com.controlself.edu.ui.screens.student.components.StreakCard
import com.controlself.edu.ui.theme.CseBlue
import com.controlself.edu.ui.theme.CseMuted
import com.controlself.edu.ui.theme.CseSurface

@Composable
fun StudentHomeScreen(
    displayName: String,
    onCourseClick: (Course) -> Unit,
    onLogout: () -> Unit
) {
    val screenTime = LocalAppContainer.current.screenTimeRepository
    val minutes by screenTime.observeTodayMinutes()
        .collectAsStateWithLifecycle(initialValue = 18)
    val dashboard = StudentDashboardMock(socialMinutes = minutes)

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
            ScreenTimeCard(
                minutesUsed = minutes,
                limitMinutes = ScreenTimeRepository.DAILY_LIMIT_MINUTES
            )
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
