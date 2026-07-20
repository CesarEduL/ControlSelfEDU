package com.controlself.edu.ui.screens.student.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.outlined.Forum
import androidx.compose.material.icons.outlined.Science
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.controlself.edu.domain.model.Course
import com.controlself.edu.ui.theme.CseOnSurfaceVariant
import com.controlself.edu.ui.theme.CseOutlineVariant
import com.controlself.edu.ui.theme.CsePrimary
import com.controlself.edu.ui.theme.CsePrimaryFixed
import com.controlself.edu.ui.theme.CsePrimaryFixedDim
import com.controlself.edu.ui.theme.CseSecondary
import com.controlself.edu.ui.theme.CseSecondaryContainer
import com.controlself.edu.ui.theme.CseOnSecondaryContainer
import com.controlself.edu.ui.theme.CseTertiaryFixed
import com.controlself.edu.ui.theme.CseTertiaryFixedDim
import com.controlself.edu.ui.theme.CseOnTertiaryFixed
import com.controlself.edu.ui.theme.CseWhite

@Composable
fun CoursesSection(
    onCourseClick: (Course) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Mis cursos",
            style = MaterialTheme.typography.labelLarge,
            color = CsePrimary,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))
        Course.entries.forEach { course ->
            CourseCard(
                course = course,
                onClick = { onCourseClick(course) },
                modifier = Modifier.padding(bottom = 12.dp)
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
    val style = course.style()
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CseWhite)
            .border(1.dp, CseOutlineVariant, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .background(style.accent)
        )
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(style.iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = course.icon(),
                    contentDescription = null,
                    tint = style.iconTint,
                    modifier = Modifier.size(28.dp)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = course.title,
                    style = MaterialTheme.typography.labelLarge,
                    color = CsePrimary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Nivel: ${course.difficulty}",
                    style = MaterialTheme.typography.labelMedium,
                    color = CseOnSurfaceVariant
                )
            }
        }
    }
}

private data class CourseStyle(
    val accent: Color,
    val iconBg: Color,
    val iconTint: Color
)

private fun Course.style(): CourseStyle = when (this) {
    Course.MATH -> CourseStyle(CseSecondary, CseSecondaryContainer, CseOnSecondaryContainer)
    Course.COMMS -> CourseStyle(CsePrimaryFixedDim, CsePrimaryFixed, CsePrimary)
    Course.SCIENCE -> CourseStyle(CseTertiaryFixedDim, CseTertiaryFixed, CseOnTertiaryFixed)
}

private fun Course.icon(): ImageVector = when (this) {
    Course.MATH -> Icons.Outlined.Calculate
    Course.COMMS -> Icons.Outlined.Forum
    Course.SCIENCE -> Icons.Outlined.Science
}
