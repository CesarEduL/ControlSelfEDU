package com.controlself.edu.ui.screens.teacher

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.controlself.edu.di.LocalAppContainer
import com.controlself.edu.domain.model.teacher.TeacherDashboard
import com.controlself.edu.ui.components.PrimaryFlatButton
import com.controlself.edu.ui.theme.CseBackground
import com.controlself.edu.ui.theme.CseOnSurfaceVariant
import com.controlself.edu.ui.theme.CseOutlineVariant
import com.controlself.edu.ui.theme.CsePrimary
import com.controlself.edu.ui.theme.CseWhite
import java.util.Locale

@Composable
fun TeacherReportsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val dashboard by LocalAppContainer.current.classroomRepository.observeDashboard()
        .collectAsStateWithLifecycle(initialValue = TeacherDashboard())
    val csv = remember(dashboard) { buildClassroomCsv(dashboard) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CseBackground)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = CsePrimary
                )
            }
            Text(
                text = "Reportes",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = CsePrimary
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                text = "Exportación CSV del salón (MVP).",
                color = CseOnSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = csv,
                style = MaterialTheme.typography.bodySmall,
                fontFamily = FontFamily.Monospace,
                color = CsePrimary,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(CseWhite)
                    .border(1.dp, CseOutlineVariant, RoundedCornerShape(16.dp))
                    .padding(12.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            PrimaryFlatButton(
                text = "Compartir / descargar CSV",
                onClick = {
                    val send = Intent(Intent.ACTION_SEND).apply {
                        type = "text/csv"
                        putExtra(Intent.EXTRA_SUBJECT, "Reporte ControlSelf EDU")
                        putExtra(Intent.EXTRA_TEXT, csv)
                    }
                    context.startActivity(Intent.createChooser(send, "Compartir reporte"))
                }
            )
        }
    }
}

private fun buildClassroomCsv(dashboard: TeacherDashboard): String {
    val sb = StringBuilder()
    sb.appendLine("nombre,promedio,evaluaciones,estado,ultimas_notas")
    dashboard.students.forEach { s ->
        val avg = s.averageScore?.let {
            String.format(Locale.US, "%.2f", it)
        } ?: ""
        val estado = if (s.isLocked) "bloqueo" else "activo"
        val notas = s.lastScores.joinToString(";")
        sb.appendLine("\"${s.displayName}\",$avg,${s.evaluationsCount},$estado,\"$notas\"")
    }
    sb.appendLine()
    sb.appendLine("# promedio_salon,${dashboard.classroomAverage ?: ""}")
    sb.appendLine("# estudiantes,${dashboard.studentCount}")
    return sb.toString()
}
