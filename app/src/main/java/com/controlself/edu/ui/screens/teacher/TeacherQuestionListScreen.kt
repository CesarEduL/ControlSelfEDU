package com.controlself.edu.ui.screens.teacher

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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.controlself.edu.di.LocalAppContainer
import com.controlself.edu.domain.model.Course
import com.controlself.edu.domain.model.quiz.QuestionType
import com.controlself.edu.domain.model.quiz.QuizAttempt
import com.controlself.edu.domain.model.teacher.CourseBankStatus
import com.controlself.edu.ui.theme.CseBlue
import com.controlself.edu.ui.theme.CseDanger
import com.controlself.edu.ui.theme.CseGreen
import com.controlself.edu.ui.theme.CseMuted
import com.controlself.edu.ui.theme.CseSurface
import com.controlself.edu.ui.theme.CseWhite
import kotlinx.coroutines.launch

@Composable
fun TeacherQuestionListScreen(
    courseId: String,
    onBack: () -> Unit,
    onEdit: (questionId: String) -> Unit,
    onAdd: () -> Unit
) {
    val container = LocalAppContainer.current
    val questions by container.quizRepository.observeQuestions(courseId)
        .collectAsStateWithLifecycle(initialValue = emptyList())
    val scope = rememberCoroutineScope()
    val courseTitle = Course.fromId(courseId)?.title ?: courseId
    val ready = questions.size == QuizAttempt.TOTAL_QUESTIONS

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CseSurface)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = courseTitle,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${questions.size} / ${CourseBankStatus.REQUIRED}" +
                        if (ready) " · Publicado" else " · Borrador",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (ready) CseGreen else CseMuted
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp)
        ) {
            Button(
                onClick = onAdd,
                modifier = Modifier.fillMaxWidth(),
                enabled = questions.size < 40
            ) {
                Text("Agregar pregunta")
            }
            Spacer(modifier = Modifier.height(12.dp))
            questions.forEachIndexed { index, question ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                        .clickable { onEdit(question.id) },
                    shape = MaterialTheme.shapes.medium,
                    color = CseWhite,
                    shadowElevation = 1.dp
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "${index + 1}. ${question.prompt}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = when (question.type) {
                                    QuestionType.MULTIPLE_CHOICE -> "Opción múltiple"
                                    QuestionType.TRUE_FALSE -> "Verdadero / Falso"
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = CseMuted
                            )
                        }
                        IconButton(
                            onClick = {
                                scope.launch {
                                    container.quizRepository.deleteQuestion(courseId, question.id)
                                }
                            }
                        ) {
                            Icon(
                                Icons.Filled.Delete,
                                contentDescription = "Eliminar",
                                tint = CseDanger
                            )
                        }
                    }
                }
            }
            if (!ready) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Para publicar, el curso debe tener exactamente ${CourseBankStatus.REQUIRED} preguntas.",
                    style = MaterialTheme.typography.bodySmall,
                    color = CseMuted
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
                Text("Volver al banco")
            }
        }
    }
}
