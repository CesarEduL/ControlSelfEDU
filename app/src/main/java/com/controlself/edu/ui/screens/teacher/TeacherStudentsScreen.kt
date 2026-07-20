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
import com.controlself.edu.domain.model.teacher.ClassroomStudent
import com.controlself.edu.ui.theme.CseDanger
import com.controlself.edu.ui.theme.CseOutlineVariant
import com.controlself.edu.ui.theme.CseGreen
import com.controlself.edu.ui.theme.CseMuted
import com.controlself.edu.ui.theme.CseBackground
import com.controlself.edu.ui.theme.CsePrimary
import com.controlself.edu.ui.theme.CseWhite
import java.util.Locale

@Composable
fun TeacherStudentsScreen(
    onBack: () -> Unit,
    onOpenStudent: (studentId: String) -> Unit
) {
    val students by LocalAppContainer.current.classroomRepository.observeStudents()
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
                text = "Estudiantes",
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
            students.forEach { student ->
                StudentRow(student = student, onClick = { onOpenStudent(student.id) })
            }
        }
    }
}

@Composable
fun TeacherStudentDetailScreen(
    studentId: String,
    onBack: () -> Unit
) {
    val repo = LocalAppContainer.current.classroomRepository
    val students by repo.observeStudents()
        .collectAsStateWithLifecycle(initialValue = emptyList())
    val student = students.find { it.id == studentId } ?: repo.getStudent(studentId)

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
                text = student?.displayName ?: "Estudiante",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
        if (student == null) {
            Text(
                text = "No encontrado",
                modifier = Modifier.padding(20.dp),
                color = CseDanger
            )
            return
        }
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = if (student.isLocked) "Estado: en bloqueo" else "Estado: activo",
                color = if (student.isLocked) CseDanger else CseGreen
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Promedio: " + (student.averageScore?.let {
                    String.format(Locale.getDefault(), "%.1f / 20", it)
                } ?: "—")
            )
            Text(text = "Evaluaciones: ${student.evaluationsCount}")
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "Últimas calificaciones", fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(6.dp))
            if (student.lastScores.isEmpty()) {
                Text("Sin intentos registrados", color = CseMuted)
            } else {
                Text(student.lastScores.joinToString(" · ") { "$it/20" })
            }
            if (student.isLocalDemo) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Datos del Estudiante Demo en este dispositivo.",
                    style = MaterialTheme.typography.bodySmall,
                    color = CseMuted
                )
            }
        }
    }
}

@Composable
private fun StudentRow(student: ClassroomStudent, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.large,
        color = CseWhite,
        border = BorderStroke(1.dp, CseOutlineVariant),
        shadowElevation = 0.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = student.displayName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Últimas: " + if (student.lastScores.isEmpty()) {
                    "—"
                } else {
                    student.lastScores.joinToString(", ") { "$it" }
                },
                style = MaterialTheme.typography.bodyMedium,
                color = CseMuted
            )
            Text(
                text = if (student.isLocked) "En bloqueo" else "Activo",
                style = MaterialTheme.typography.bodySmall,
                color = if (student.isLocked) CseDanger else CseGreen
            )
        }
    }
}
