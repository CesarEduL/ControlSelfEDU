package com.controlself.edu.ui.screens.teacher

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.controlself.edu.domain.model.teacher.CourseBankStatus
import com.controlself.edu.ui.theme.CseBlue
import com.controlself.edu.ui.theme.CseOutlineVariant
import com.controlself.edu.ui.theme.CseGreen
import com.controlself.edu.ui.theme.CseMuted
import com.controlself.edu.ui.theme.CseBackground
import com.controlself.edu.ui.theme.CsePrimary
import com.controlself.edu.ui.theme.CseWarning
import com.controlself.edu.ui.theme.CseWhite

@Composable
fun TeacherBankScreen(
    onBack: () -> Unit,
    onOpenCourse: (courseId: String) -> Unit
) {
    val statuses by LocalAppContainer.current.quizRepository.observeBankStatus()
        .collectAsStateWithLifecycle(initialValue = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CseBackground)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = CsePrimary)
            }
            Text(
                text = "Banco de preguntas",
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
            Text(
                text = "Cada curso publica una evaluación de ${CourseBankStatus.REQUIRED} preguntas.",
                style = MaterialTheme.typography.bodyMedium,
                color = CseMuted
            )
            Spacer(modifier = Modifier.height(16.dp))
            statuses.forEach { status ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                        .clickable { onOpenCourse(status.courseId) },
                    shape = MaterialTheme.shapes.large,
                    color = CseWhite,
                    border = BorderStroke(1.dp, CseOutlineVariant),
                    shadowElevation = 0.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = status.courseTitle,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${status.questionCount} / ${CourseBankStatus.REQUIRED} preguntas",
                            color = if (status.isReady) CseGreen else CseWarning
                        )
                        Text(
                            text = if (status.isReady) "Listo para estudiantes" else "Incompleto — editar banco",
                            style = MaterialTheme.typography.bodySmall,
                            color = CseMuted
                        )
                    }
                }
            }
        }
    }
}
