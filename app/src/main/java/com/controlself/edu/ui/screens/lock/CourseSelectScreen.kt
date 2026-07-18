package com.controlself.edu.ui.screens.lock

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.controlself.edu.domain.model.Course
import com.controlself.edu.ui.screens.student.components.CoursesSection
import com.controlself.edu.ui.theme.CseBlue
import com.controlself.edu.ui.theme.CseMuted
import com.controlself.edu.ui.theme.CseSurface

@Composable
fun CourseSelectScreen(
    fromLock: Boolean,
    onCourseClick: (Course) -> Unit,
    onBack: () -> Unit
) {
    if (fromLock) {
        BackHandler(onBack = onBack)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CseSurface)
    ) {
        IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
        ) {
            Text(
                text = "Elige un curso",
                style = MaterialTheme.typography.headlineMedium,
                color = CseBlue,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (fromLock) {
                    "Completa una lección para desbloquear el entretenimiento."
                } else {
                    "Selecciona la materia que quieres practicar."
                },
                style = MaterialTheme.typography.bodyLarge,
                color = CseMuted
            )
            Spacer(modifier = Modifier.height(20.dp))
            CoursesSection(onCourseClick = onCourseClick)
        }
    }
}
