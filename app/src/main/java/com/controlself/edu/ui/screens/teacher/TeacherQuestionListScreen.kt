package com.controlself.edu.ui.screens.teacher

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.controlself.edu.di.LocalAppContainer
import com.controlself.edu.domain.model.Course
import com.controlself.edu.domain.model.quiz.Question
import com.controlself.edu.domain.model.quiz.QuestionType
import com.controlself.edu.domain.model.quiz.QuizAttempt
import com.controlself.edu.domain.model.teacher.CourseBankStatus
import com.controlself.edu.ui.components.SecondaryFlatButton
import com.controlself.edu.ui.theme.CseBackground
import com.controlself.edu.ui.theme.CseError
import com.controlself.edu.ui.theme.CseOnSecondaryContainer
import com.controlself.edu.ui.theme.CseOnSurfaceVariant
import com.controlself.edu.ui.theme.CseOutlineVariant
import com.controlself.edu.ui.theme.CsePrimary
import com.controlself.edu.ui.theme.CseSecondary
import com.controlself.edu.ui.theme.CseSecondaryContainer
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

    Scaffold(
        containerColor = CseBackground,
        floatingActionButton = {
            if (questions.size < 40) {
                ExtendedFloatingActionButton(
                    onClick = onAdd,
                    containerColor = CsePrimary,
                    contentColor = CseWhite,
                    icon = { Icon(Icons.Filled.Add, contentDescription = null) },
                    text = { Text("Crear pregunta", fontWeight = FontWeight.Bold) }
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = CsePrimary
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = courseTitle,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = CsePrimary
                    )
                    Text(
                        text = "${questions.size} / ${CourseBankStatus.REQUIRED}" +
                            if (ready) " · Publicado" else " · Borrador",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (ready) CseSecondary else CseOnSurfaceVariant
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 88.dp)
            ) {
                Text(
                    text = "Preguntas del curso",
                    style = MaterialTheme.typography.headlineMedium,
                    color = CsePrimary,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = "Toca una pregunta para editarla.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = CseOnSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                )

                questions.forEachIndexed { index, question ->
                    QuestionBankItem(
                        index = index + 1,
                        courseTitle = courseTitle,
                        question = question,
                        onEdit = { onEdit(question.id) },
                        onDelete = {
                            scope.launch {
                                container.quizRepository.deleteQuestion(courseId, question.id)
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }

                if (!ready) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Para publicar, el curso debe tener exactamente ${CourseBankStatus.REQUIRED} preguntas.",
                        style = MaterialTheme.typography.bodySmall,
                        color = CseOnSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                SecondaryFlatButton(text = "Volver al banco", onClick = onBack)
            }
        }
    }
}

@Composable
private fun QuestionBankItem(
    index: Int,
    courseTitle: String,
    question: Question,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onEdit),
        shape = RoundedCornerShape(16.dp),
        color = CseWhite,
        border = BorderStroke(1.dp, CseOutlineVariant),
        shadowElevation = 0.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = courseTitle.take(12),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = CseOnSecondaryContainer,
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(CseSecondaryContainer)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))
                if (!question.imagePath.isNullOrBlank()) {
                    Icon(
                        Icons.Outlined.Image,
                        contentDescription = "Con imagen",
                        tint = CseSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.size(4.dp))
                }
                Text(
                    text = when (question.type) {
                        QuestionType.MULTIPLE_CHOICE -> "Opción múltiple"
                        QuestionType.TRUE_FALSE -> "V / F"
                    },
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = CseOnSurfaceVariant,
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(CseBackground)
                        .border(1.dp, CseOutlineVariant, RoundedCornerShape(50))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "#$index",
                    style = MaterialTheme.typography.labelSmall,
                    color = CseOnSurfaceVariant,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = question.prompt,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = CsePrimary,
                maxLines = 3
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = question.options.joinToString(" · ") { it.take(24) },
                style = MaterialTheme.typography.bodySmall,
                color = CseOnSurfaceVariant,
                maxLines = 2
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onEdit, modifier = Modifier.size(36.dp)) {
                    Icon(Icons.Outlined.Edit, contentDescription = "Editar", tint = CseSecondary)
                }
                IconButton(onClick = onDelete, modifier = Modifier.size(36.dp)) {
                    Icon(Icons.Filled.Delete, contentDescription = "Eliminar", tint = CseError)
                }
            }
        }
    }
}
