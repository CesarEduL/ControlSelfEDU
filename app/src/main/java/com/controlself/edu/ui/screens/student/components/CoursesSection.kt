package com.controlself.edu.ui.screens.student.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Science
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.controlself.edu.domain.model.Course
import com.controlself.edu.ui.theme.CseBlue
import com.controlself.edu.ui.theme.CseGreen
import com.controlself.edu.ui.theme.CseTeal
import com.controlself.edu.ui.theme.CseWhite

@Composable
fun CoursesSection(
    onCourseClick: (Course) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Mis cursos",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(12.dp))
        Course.entries.forEach { course ->
            CourseCard(
                course = course,
                onClick = { onCourseClick(course) },
                modifier = Modifier.padding(bottom = 10.dp)
            )
        }
    }
}

@Composable
private fun CourseCard(
    course: Course,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val accent = course.accentColor()
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = accent,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = course.icon(),
                contentDescription = null,
                tint = CseWhite,
                modifier = Modifier.size(36.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = course.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = CseWhite,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Nivel: ${course.difficulty}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = CseWhite.copy(alpha = 0.9f)
                )
            }
        }
    }
}

private fun Course.accentColor(): Color = when (this) {
    Course.MATH -> CseBlue
    Course.COMMS -> CseGreen
    Course.SCIENCE -> CseTeal
}

private fun Course.icon(): ImageVector = when (this) {
    Course.MATH -> Icons.Outlined.Calculate
    Course.COMMS -> Icons.Outlined.MenuBook
    Course.SCIENCE -> Icons.Outlined.Science
}
