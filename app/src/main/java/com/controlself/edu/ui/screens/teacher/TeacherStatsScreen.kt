package com.controlself.edu.ui.screens.teacher

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.controlself.edu.di.LocalAppContainer
import com.controlself.edu.domain.model.Course
import com.controlself.edu.ui.theme.CseDanger
import com.controlself.edu.ui.theme.CseMuted
import com.controlself.edu.ui.theme.CseSurface
import com.controlself.edu.ui.theme.CseWhite

@Composable
fun TeacherStatsScreen(onBack: () -> Unit) {
    val difficulties by LocalAppContainer.current.questionAnalyticsRepository.observeDifficulties()
        .collectAsStateWithLifecycle(initialValue = emptyList())
    val hard = difficulties.filter { it.attempts > 0 }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CseSurface)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }
            Text(
                text = "Temas difíciles",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp)
        ) {
            if (hard.isEmpty()) {
                Text(
                    text = "Aún no hay datos. Cuando los estudiantes respondan, verás el ranking de errores.",
                    color = CseMuted
                )
            } else {
                Text(
                    text = "Ordenado por % de error (mayor primero).",
                    style = MaterialTheme.typography.bodyMedium,
                    color = CseMuted
                )
                Spacer(modifier = Modifier.height(12.dp))
                hard.forEachIndexed { index, topic ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        shape = MaterialTheme.shapes.medium,
                        color = CseWhite,
                        shadowElevation = 1.dp
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "${index + 1}. ${topic.prompt}",
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = Course.fromId(topic.courseId)?.title ?: topic.courseId,
                                style = MaterialTheme.typography.bodySmall,
                                color = CseMuted
                            )
                            Text(
                                text = "Error ${topic.errorPercent}% · acierto ${topic.accuracyPercent}% · ${topic.wrongCount}/${topic.attempts}",
                                color = CseDanger,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }
    }
}
