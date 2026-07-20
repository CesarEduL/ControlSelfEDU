package com.controlself.edu.ui.screens.teacher

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.controlself.edu.di.LocalAppContainer
import com.controlself.edu.domain.model.teacher.TeacherDashboard
import com.controlself.edu.ui.components.PrimaryFlatButton
import com.controlself.edu.ui.components.SecondaryFlatButton
import com.controlself.edu.ui.theme.CseBackground
import com.controlself.edu.ui.theme.CseError
import com.controlself.edu.ui.theme.CseErrorContainer
import com.controlself.edu.ui.theme.CseOnSurfaceVariant
import com.controlself.edu.ui.theme.CseOutlineVariant
import com.controlself.edu.ui.theme.CsePrimary
import com.controlself.edu.ui.theme.CseWhite
import java.util.Locale

@Composable
fun TeacherHomeScreen(
    displayName: String,
    onOpenBank: () -> Unit,
    onOpenStudents: () -> Unit,
    onOpenStats: () -> Unit,
    onOpenReports: () -> Unit,
    onOpenProtection: () -> Unit,
    onLogout: () -> Unit
) {
    val dashboard by LocalAppContainer.current.classroomRepository.observeDashboard()
        .collectAsStateWithLifecycle(initialValue = TeacherDashboard())

    TeacherHomeContent(
        displayName = displayName,
        dashboard = dashboard,
        onOpenBank = onOpenBank,
        onOpenStudents = onOpenStudents,
        onOpenStats = onOpenStats,
        onOpenReports = onOpenReports,
        onOpenProtection = onOpenProtection,
        onLogout = onLogout
    )
}

@Composable
private fun TeacherHomeContent(
    displayName: String,
    dashboard: TeacherDashboard,
    onOpenBank: () -> Unit,
    onOpenStudents: () -> Unit,
    onOpenStats: () -> Unit,
    onOpenReports: () -> Unit,
    onOpenProtection: () -> Unit,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CseBackground)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = onLogout) {
                Text("Cerrar sesión", color = CsePrimary, fontWeight = FontWeight.Bold)
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(bottom = 32.dp)
        ) {
            Text(
                text = "Panel docente",
                style = MaterialTheme.typography.headlineLarge,
                color = CsePrimary,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Hola, $displayName",
                style = MaterialTheme.typography.titleMedium,
                color = CseOnSurfaceVariant
            )
            Spacer(modifier = Modifier.height(20.dp))

            MetricCard(
                title = "Estudiantes",
                value = dashboard.studentCount.toString()
            )
            Spacer(modifier = Modifier.height(12.dp))
            MetricCard(
                title = "Promedio del salón",
                value = dashboard.classroomAverage?.let {
                    String.format(Locale.getDefault(), "%.1f / 20", it)
                } ?: "—"
            )
            Spacer(modifier = Modifier.height(12.dp))
            MetricCard(
                title = "Cursos listos (20 preguntas)",
                value = "${dashboard.publishedCourses} / 3"
            )

            if (dashboard.hardTopicsPreview.isNotEmpty()) {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Temas difíciles",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = CsePrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                dashboard.hardTopicsPreview.forEach { topic ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        shape = MaterialTheme.shapes.medium,
                        color = CseWhite,
                        border = BorderStroke(1.dp, CseOutlineVariant),
                        shadowElevation = 0.dp
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = topic.prompt,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                color = CsePrimary
                            )
                            Text(
                                text = "Error ${topic.errorPercent}% · ${topic.wrongCount}/${topic.attempts}",
                                style = MaterialTheme.typography.bodySmall,
                                color = CseError,
                                modifier = Modifier
                                    .padding(top = 4.dp)
                                    .background(CseErrorContainer.copy(alpha = 0.5f))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            PrimaryFlatButton(text = "Administrar banco de preguntas", onClick = onOpenBank)
            Spacer(modifier = Modifier.height(10.dp))
            SecondaryFlatButton(text = "Ver estudiantes", onClick = onOpenStudents)
            Spacer(modifier = Modifier.height(10.dp))
            SecondaryFlatButton(text = "Estadísticas y temas difíciles", onClick = onOpenStats)
            Spacer(modifier = Modifier.height(10.dp))
            SecondaryFlatButton(text = "Descargar reportes", onClick = onOpenReports)
            Spacer(modifier = Modifier.height(10.dp))
            SecondaryFlatButton(text = "Protección anti-desinstalación", onClick = onOpenProtection)
        }
    }
}

@Composable
private fun MetricCard(title: String, value: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = CseWhite,
        border = BorderStroke(1.dp, CseOutlineVariant),
        shadowElevation = 0.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                color = CseOnSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = CsePrimary
            )
        }
    }
}
