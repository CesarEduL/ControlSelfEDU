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
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.controlself.edu.di.LocalAppContainer
import com.controlself.edu.domain.model.quiz.Question
import com.controlself.edu.domain.model.quiz.QuestionType
import com.controlself.edu.ui.theme.CseDanger
import com.controlself.edu.ui.theme.CseMuted
import com.controlself.edu.ui.theme.CseBackground
import com.controlself.edu.ui.theme.CsePrimary
import kotlinx.coroutines.launch
import java.util.UUID

@Composable
fun TeacherQuestionEditScreen(
    courseId: String,
    questionId: String?,
    onDone: () -> Unit,
    onBack: () -> Unit
) {
    val container = LocalAppContainer.current
    val questions by container.quizRepository.observeQuestions(courseId)
        .collectAsStateWithLifecycle(initialValue = emptyList())
    val existing = questionId?.let { id -> questions.find { it.id == id } }
    val isNew = questionId == null || questionId == "new"

    var type by remember(existing?.id) {
        mutableStateOf(existing?.type ?: QuestionType.MULTIPLE_CHOICE)
    }
    var prompt by remember(existing?.id) { mutableStateOf(existing?.prompt.orEmpty()) }
    var option0 by remember(existing?.id) {
        mutableStateOf(existing?.options?.getOrNull(0).orEmpty())
    }
    var option1 by remember(existing?.id) {
        mutableStateOf(existing?.options?.getOrNull(1).orEmpty())
    }
    var option2 by remember(existing?.id) {
        mutableStateOf(existing?.options?.getOrNull(2).orEmpty())
    }
    var option3 by remember(existing?.id) {
        mutableStateOf(existing?.options?.getOrNull(3).orEmpty())
    }
    var correctIndex by remember(existing?.id) {
        mutableIntStateOf(existing?.correctIndex ?: 0)
    }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

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
                text = if (isNew) "Nueva pregunta" else "Editar pregunta",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            Text("Tipo", style = MaterialTheme.typography.labelLarge, color = CseMuted)
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                FilterChip(
                    selected = type == QuestionType.MULTIPLE_CHOICE,
                    onClick = {
                        type = QuestionType.MULTIPLE_CHOICE
                        if (correctIndex > 3) correctIndex = 0
                    },
                    label = { Text("Opción múltiple") },
                    modifier = Modifier.padding(end = 8.dp)
                )
                FilterChip(
                    selected = type == QuestionType.TRUE_FALSE,
                    onClick = {
                        type = QuestionType.TRUE_FALSE
                        option0 = "Verdadero"
                        option1 = "Falso"
                        option2 = ""
                        option3 = ""
                        if (correctIndex > 1) correctIndex = 0
                    },
                    label = { Text("V / F") }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = prompt,
                onValueChange = { prompt = it },
                label = { Text("Enunciado") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )
            Spacer(modifier = Modifier.height(12.dp))

            if (type == QuestionType.TRUE_FALSE) {
                Text("Respuesta correcta", color = CseMuted)
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    FilterChip(
                        selected = correctIndex == 0,
                        onClick = { correctIndex = 0 },
                        label = { Text("Verdadero") },
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    FilterChip(
                        selected = correctIndex == 1,
                        onClick = { correctIndex = 1 },
                        label = { Text("Falso") }
                    )
                }
            } else {
                listOf(
                    0 to option0,
                    1 to option1,
                    2 to option2,
                    3 to option3
                ).forEach { (index, value) ->
                    OutlinedTextField(
                        value = value,
                        onValueChange = {
                            when (index) {
                                0 -> option0 = it
                                1 -> option1 = it
                                2 -> option2 = it
                                else -> option3 = it
                            }
                        },
                        label = { Text("Opción ${index + 1}") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    )
                }
                Text("Índice correcto (0–3)", color = CseMuted)
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    (0..3).forEach { i ->
                        FilterChip(
                            selected = correctIndex == i,
                            onClick = { correctIndex = i },
                            label = { Text("${i + 1}") },
                            modifier = Modifier.padding(end = 6.dp)
                        )
                    }
                }
            }

            error?.let {
                Spacer(modifier = Modifier.height(12.dp))
                Text(it, color = CseDanger)
            }

            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    val options = if (type == QuestionType.TRUE_FALSE) {
                        listOf("Verdadero", "Falso")
                    } else {
                        listOf(option0, option1, option2, option3)
                            .map { it.trim() }
                            .filter { it.isNotEmpty() }
                    }
                    when {
                        prompt.isBlank() -> error = "Escribe el enunciado"
                        options.size < 2 -> error = "Se necesitan al menos 2 opciones"
                        correctIndex !in options.indices -> error = "Marca una opción correcta válida"
                        else -> {
                            error = null
                            val question = Question(
                                id = questionId ?: UUID.randomUUID().toString(),
                                prompt = prompt.trim(),
                                type = type,
                                options = options,
                                correctIndex = correctIndex
                            )
                            scope.launch {
                                container.quizRepository.upsertQuestion(courseId, question)
                                onDone()
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar")
            }
        }
    }
}
