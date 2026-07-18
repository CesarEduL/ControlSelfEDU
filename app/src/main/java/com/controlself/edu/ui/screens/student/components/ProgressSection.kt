package com.controlself.edu.ui.screens.student.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.controlself.edu.domain.model.StudentDashboardMock
import com.controlself.edu.ui.theme.CseMuted
import com.controlself.edu.ui.theme.CseWhite

@Composable
fun ProgressSection(
    mock: StudentDashboardMock,
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
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Vista simple — gráficos en PRP-10",
                style = MaterialTheme.typography.bodySmall,
                color = CseMuted
            )
            Spacer(modifier = Modifier.height(12.dp))
            ProgressRow("Promedio de notas", mock.averageScore)
            ProgressRow("Lecciones completadas", mock.lessonsCompleted.toString())
            ProgressRow("Tiempo estudiado", "${mock.studyMinutes} min")
            ProgressRow("Tiempo en redes", "${mock.socialMinutes} min")
            ProgressRow("Curso favorito", mock.favoriteCourse, showDivider = false)
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
        HorizontalDivider(color = CseMuted.copy(alpha = 0.15f))
    }
}
