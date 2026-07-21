package com.controlself.edu.ui.screens.teacher

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Quiz
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.controlself.edu.di.LocalAppContainer
import com.controlself.edu.domain.model.quiz.QuizAttempt
import com.controlself.edu.domain.model.teacher.ClassroomStudent
import com.controlself.edu.domain.model.teacher.QuestionDifficulty
import com.controlself.edu.domain.model.teacher.TeacherDashboard
import com.controlself.edu.ui.components.PrimaryFlatButton
import com.controlself.edu.ui.components.SecondaryFlatButton
import com.controlself.edu.ui.screens.teacher.components.TeacherBottomBar
import com.controlself.edu.ui.screens.teacher.components.TeacherTab
import com.controlself.edu.ui.theme.CseBackground
import com.controlself.edu.ui.theme.CseError
import com.controlself.edu.ui.theme.CseErrorContainer
import com.controlself.edu.ui.theme.CseOnSecondaryContainer
import com.controlself.edu.ui.theme.CseOnSurfaceVariant
import com.controlself.edu.ui.theme.CseOutlineVariant
import com.controlself.edu.ui.theme.CsePrimary
import com.controlself.edu.ui.theme.CsePrimaryContainer
import com.controlself.edu.ui.theme.CsePrimaryFixed
import com.controlself.edu.ui.theme.CsePrimaryFixedDim
import com.controlself.edu.ui.theme.CseSecondary
import com.controlself.edu.ui.theme.CseSecondaryContainer
import com.controlself.edu.ui.theme.CseSurfaceLow
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
    var tab by remember { mutableStateOf(TeacherTab.HOME) }

    Scaffold(
        containerColor = CseBackground,
        bottomBar = {
            TeacherBottomBar(selected = tab, onSelect = { tab = it })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            TeacherTopBar(displayName = displayName)
            when (tab) {
                TeacherTab.HOME -> HomeTab(
                    displayName = displayName,
                    dashboard = dashboard,
                    onOpenBank = onOpenBank,
                    onOpenStudents = onOpenStudents,
                    onOpenReports = onOpenReports
                )
                TeacherTab.CLASSES -> ClassesTab(
                    dashboard = dashboard,
                    onOpenBank = onOpenBank,
                    onOpenStudents = onOpenStudents
                )
                TeacherTab.REPORTS -> ReportsTab(
                    dashboard = dashboard,
                    onOpenStats = onOpenStats,
                    onOpenReports = onOpenReports
                )
                TeacherTab.PROFILE -> ProfileTab(
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
private fun TeacherTopBar(displayName: String) {
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
private fun HomeTab(
    displayName: String,
    dashboard: TeacherDashboard,
    onOpenBank: () -> Unit,
    onOpenStudents: () -> Unit,
    onOpenReports: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Bienvenido, $displayName",
            style = MaterialTheme.typography.headlineLarge,
            color = CsePrimary,
            fontWeight = FontWeight.ExtraBold
        )
        Text(
            text = "Resumen de tu salón y acciones rápidas.",
            style = MaterialTheme.typography.bodyMedium,
            color = CseOnSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
        )

        InsightsCard(dashboard = dashboard)
        Spacer(modifier = Modifier.height(12.dp))

        SectionLabel("ACCIONES RÁPIDAS")
        Spacer(modifier = Modifier.height(8.dp))
        QuickActionButton(
            icon = Icons.Outlined.Quiz,
            label = "Banco de preguntas",
            primary = true,
            onClick = onOpenBank
        )
        Spacer(modifier = Modifier.height(8.dp))
        QuickActionButton(
            icon = Icons.Outlined.Group,
            label = "Ver estudiantes",
            primary = false,
            onClick = onOpenStudents
        )
        Spacer(modifier = Modifier.height(8.dp))
        QuickActionButton(
            icon = Icons.Outlined.Analytics,
            label = "Ver reportes",
            primary = false,
            onClick = onOpenReports
        )

        if (dashboard.hardTopicsPreview.isNotEmpty()) {
            Spacer(modifier = Modifier.height(20.dp))
            SectionLabel("NECESITAN ATENCIÓN")
            Spacer(modifier = Modifier.height(8.dp))
            dashboard.hardTopicsPreview.take(3).forEach { topic ->
                HardTopicRow(topic = topic)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        SectionLabel("ACTIVIDAD RECIENTE")
        Spacer(modifier = Modifier.height(8.dp))
        ActivityCard(
            title = "${dashboard.studentCount} estudiantes en el salón",
            subtitle = "${dashboard.publishedCourses} / 3 cursos listos para evaluar"
        )
    }
}

@Composable
private fun ClassesTab(
    dashboard: TeacherDashboard,
    onOpenBank: () -> Unit,
    onOpenStudents: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Clases",
            style = MaterialTheme.typography.headlineLarge,
            color = CsePrimary,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Vista general del salón demo.",
            color = CseOnSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))

        ClassOverviewCard(
            title = "Salón principal",
            subtitle = "Demo · ${dashboard.studentCount} estudiantes",
            status = if (dashboard.publishedCourses >= 3) "Listo" else "Activo",
            detail = "${dashboard.publishedCourses}/3 cursos publicados"
        )
        Spacer(modifier = Modifier.height(12.dp))
        PrimaryFlatButton(text = "Administrar banco", onClick = onOpenBank)
        Spacer(modifier = Modifier.height(8.dp))
        SecondaryFlatButton(text = "Lista de estudiantes", onClick = onOpenStudents)

        Spacer(modifier = Modifier.height(20.dp))
        SectionLabel("ESTUDIANTES")
        Spacer(modifier = Modifier.height(8.dp))
        dashboard.students.take(5).forEach { student ->
            StudentAttentionRow(student = student)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun ReportsTab(
    dashboard: TeacherDashboard,
    onOpenStats: () -> Unit,
    onOpenReports: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Reportes",
            style = MaterialTheme.typography.headlineLarge,
            color = CsePrimary,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.height(16.dp))
        InsightsCard(dashboard = dashboard)
        Spacer(modifier = Modifier.height(12.dp))
        PrimaryFlatButton(text = "Temas difíciles", onClick = onOpenStats)
        Spacer(modifier = Modifier.height(8.dp))
        SecondaryFlatButton(text = "Exportar CSV", onClick = onOpenReports)
    }
}

@Composable
private fun ProfileTab(
    displayName: String,
    dashboard: TeacherDashboard,
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
        Text("Docente", color = CseOnSurfaceVariant)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "${dashboard.studentCount} estudiantes · ${dashboard.publishedCourses} cursos listos",
            color = CseOnSurfaceVariant,
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.height(24.dp))
        QuickActionButton(
            icon = Icons.Outlined.Security,
            label = "Protección anti-desinstalación",
            primary = false,
            onClick = onOpenProtection
        )
        Spacer(modifier = Modifier.height(10.dp))
        SecondaryFlatButton(text = "Cerrar sesión", onClick = onLogout)
    }
}

@Composable
private fun InsightsCard(dashboard: TeacherDashboard) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = CseWhite,
        border = BorderStroke(1.dp, CseOutlineVariant),
        shadowElevation = 0.dp
    ) {
        Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(CseSecondaryContainer)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "PROMEDIO DEL SALÓN",
                style = MaterialTheme.typography.labelSmall,
                color = CseOnSurfaceVariant,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = dashboard.classroomAverage?.let {
                    String.format(
                        Locale.getDefault(),
                        "%.0f%%",
                        it / QuizAttempt.TOTAL_QUESTIONS.toDouble() * 100
                    )
                } ?: "—",
                style = MaterialTheme.typography.displayMedium,
                color = CsePrimary,
                fontWeight = FontWeight.ExtraBold
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Outlined.TrendingUp,
                    null,
                    tint = CseSecondary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = dashboard.classroomAverage?.let {
                        String.format(
                            Locale.getDefault(),
                            "%.1f / %d",
                            it,
                            QuizAttempt.TOTAL_QUESTIONS
                        )
                    } ?: "Sin datos aún",
                    style = MaterialTheme.typography.labelMedium,
                    color = CseSecondary,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "${dashboard.studentCount} estudiantes · ${dashboard.publishedCourses}/3 cursos listos",
                style = MaterialTheme.typography.bodySmall,
                color = CseOnSurfaceVariant
            )
        }
    }
}

@Composable
private fun ClassOverviewCard(
    title: String,
    subtitle: String,
    status: String,
    detail: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = CseWhite,
        border = BorderStroke(1.dp, CseOutlineVariant),
        shadowElevation = 0.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = status,
                style = MaterialTheme.typography.labelSmall,
                color = CseOnSecondaryContainer,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(CseSecondaryContainer)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                color = CsePrimary,
                fontWeight = FontWeight.Bold
            )
            Text(subtitle, color = CseOnSurfaceVariant)
            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.Group, null, tint = CseOnSurfaceVariant, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(detail, style = MaterialTheme.typography.labelMedium, color = CseOnSurfaceVariant)
            }
        }
    }
}

@Composable
private fun QuickActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    primary: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (primary) CsePrimary else CseWhite)
            .then(
                if (primary) Modifier else Modifier.border(2.dp, CsePrimary, RoundedCornerShape(12.dp))
            )
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = if (primary) CseWhite else CsePrimary
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            color = if (primary) CseWhite else CsePrimary,
            fontWeight = FontWeight.Bold
        )
        Icon(
            Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = if (primary) CseWhite else CsePrimary
        )
    }
}

@Composable
private fun HardTopicRow(topic: QuestionDifficulty) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(CseSurfaceLow)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(CseErrorContainer),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${topic.errorPercent}%",
                style = MaterialTheme.typography.labelSmall,
                color = CseError,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = topic.prompt,
                maxLines = 2,
                fontWeight = FontWeight.SemiBold,
                color = CsePrimary,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Error ${topic.wrongCount}/${topic.attempts}",
                style = MaterialTheme.typography.labelSmall,
                color = CseError
            )
        }
    }
}

@Composable
private fun StudentAttentionRow(student: ClassroomStudent) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(CseSurfaceLow)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(if (student.isLocked) CseErrorContainer else CsePrimaryFixed),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = student.displayName.take(2).uppercase(),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = if (student.isLocked) CseError else CsePrimary
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(student.displayName, fontWeight = FontWeight.Bold, color = CsePrimary)
            Text(
                text = when {
                    student.isLocked -> "En bloqueo"
                    student.averageScore == null -> "Sin evaluaciones"
                    else -> String.format(
                        Locale.getDefault(),
                        "Promedio %.1f / %d",
                        student.averageScore,
                        QuizAttempt.TOTAL_QUESTIONS
                    )
                },
                style = MaterialTheme.typography.labelSmall,
                color = if (student.isLocked) CseError else CseOnSurfaceVariant
            )
        }
    }
}

@Composable
private fun ActivityCard(title: String, subtitle: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CseWhite)
            .border(1.dp, CseOutlineVariant, RoundedCornerShape(16.dp))
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(CseSecondaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Outlined.Group, null, tint = CseOnSecondaryContainer)
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(title, fontWeight = FontWeight.SemiBold, color = CsePrimary)
            Text(subtitle, style = MaterialTheme.typography.labelSmall, color = CseOnSurfaceVariant)
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        color = CseOnSurfaceVariant,
        fontWeight = FontWeight.Bold
    )
}
