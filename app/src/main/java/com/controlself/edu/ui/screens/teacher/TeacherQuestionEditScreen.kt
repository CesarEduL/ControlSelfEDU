package com.controlself.edu.ui.screens.teacher

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.controlself.edu.data.quiz.QuestionImageStorage
import com.controlself.edu.di.LocalAppContainer
import com.controlself.edu.domain.model.Course
import com.controlself.edu.domain.model.quiz.Question
import com.controlself.edu.domain.model.quiz.QuestionType
import com.controlself.edu.ui.components.PrimaryFlatButton
import com.controlself.edu.ui.components.QuestionPromptMedia
import com.controlself.edu.ui.components.SecondaryFlatButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.controlself.edu.ui.theme.CseBackground
import com.controlself.edu.ui.theme.CseError
import com.controlself.edu.ui.theme.CseOnSecondaryContainer
import com.controlself.edu.ui.theme.CseOnSurfaceVariant
import com.controlself.edu.ui.theme.CseOutlineVariant
import com.controlself.edu.ui.theme.CsePrimary
import com.controlself.edu.ui.theme.CseSecondary
import com.controlself.edu.ui.theme.CseSecondaryContainer
import com.controlself.edu.ui.theme.CseSurfaceLow
import com.controlself.edu.ui.theme.CseWhite
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
    val stableQuestionId = remember(questionId) {
        if (isNew) UUID.randomUUID().toString() else questionId!!
    }
    val context = LocalContext.current
    val courseTitle = Course.fromId(courseId)?.title ?: courseId

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
    var imagePath by remember(existing?.id) { mutableStateOf(existing?.imagePath) }
    var pendingImageUri by remember { mutableStateOf<Uri?>(null) }
    var imageRemoved by remember(existing?.id) { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    val pickImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            pendingImageUri = uri
            imageRemoved = false
        }
    }

    val hasImagePreview = pendingImageUri != null || (!imageRemoved && !imagePath.isNullOrBlank())

    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = CseSecondary,
        unfocusedBorderColor = CseOutlineVariant,
        focusedContainerColor = CseWhite,
        unfocusedContainerColor = CseWhite
    )

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
                text = if (isNew) "Nueva pregunta" else "Editar pregunta",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = CsePrimary
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(bottom = 32.dp)
        ) {
            Text(
                text = "Banco / $courseTitle",
                style = MaterialTheme.typography.labelMedium,
                color = CseOnSurfaceVariant
            )
            Text(
                text = if (isNew) "Crear pregunta" else "Detalle de pregunta",
                style = MaterialTheme.typography.headlineLarge,
                color = CsePrimary,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
            )

            FormCard {
                Text(
                    text = "Enunciado",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = CsePrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = prompt,
                    onValueChange = { prompt = it },
                    placeholder = { Text("Escribe el enunciado…") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = fieldColors
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Imagen (opcional)",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = CseOnSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    SecondaryFlatButton(
                        text = if (hasImagePreview) "Cambiar imagen" else "Añadir imagen",
                        onClick = {
                            pickImage.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        modifier = Modifier.weight(1f)
                    )
                    if (hasImagePreview) {
                        Spacer(modifier = Modifier.width(8.dp))
                        SecondaryFlatButton(
                            text = "Quitar",
                            onClick = {
                                pendingImageUri = null
                                imageRemoved = true
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            FormCard {
                Text(
                    text = "Tipo de pregunta",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = CsePrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    FilterChip(
                        selected = type == QuestionType.MULTIPLE_CHOICE,
                        onClick = {
                            type = QuestionType.MULTIPLE_CHOICE
                            if (correctIndex > 3) correctIndex = 0
                        },
                        label = { Text("Opción múltiple") },
                        modifier = Modifier.padding(end = 8.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = CseSecondaryContainer,
                            selectedLabelColor = CseOnSecondaryContainer
                        )
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
                        label = { Text("V / F") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = CseSecondaryContainer,
                            selectedLabelColor = CseOnSecondaryContainer
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            FormCard {
                Text(
                    text = "Opciones de respuesta",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = CsePrimary
                )
                Spacer(modifier = Modifier.height(12.dp))

                if (type == QuestionType.TRUE_FALSE) {
                    OptionRow(
                        label = "Verdadero",
                        selected = correctIndex == 0,
                        onSelect = { correctIndex = 0 }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OptionRow(
                        label = "Falso",
                        selected = correctIndex == 1,
                        onSelect = { correctIndex = 1 }
                    )
                } else {
                    listOf(
                        0 to option0,
                        1 to option1,
                        2 to option2,
                        3 to option3
                    ).forEach { (index, value) ->
                        OptionEditableRow(
                            index = index,
                            value = value,
                            selected = correctIndex == index,
                            onValueChange = {
                                when (index) {
                                    0 -> option0 = it
                                    1 -> option1 = it
                                    2 -> option2 = it
                                    else -> option3 = it
                                }
                            },
                            onSelect = { correctIndex = index },
                            fieldColors = fieldColors
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(CseSurfaceLow)
                    .border(2.dp, CseSecondary.copy(alpha = 0.35f), RoundedCornerShape(16.dp))
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(Icons.Outlined.Visibility, null, tint = CseSecondary, modifier = Modifier.size(32.dp))
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Vista previa",
                    fontWeight = FontWeight.Bold,
                    color = CsePrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                QuestionPromptMedia(
                    prompt = prompt,
                    imagePath = if (imageRemoved) null else imagePath,
                    pendingImageUri = pendingImageUri,
                    promptPlaceholder = "El enunciado aparecerá aquí"
                )
            }

            error?.let {
                Spacer(modifier = Modifier.height(12.dp))
                Text(it, color = CseError)
            }

            Spacer(modifier = Modifier.height(20.dp))
            PrimaryFlatButton(
                text = "Guardar en el banco",
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
                            scope.launch {
                                var finalImagePath: String? = if (imageRemoved) {
                                    null
                                } else {
                                    imagePath
                                }
                                if (imageRemoved || pendingImageUri != null) {
                                    QuestionImageStorage.delete(context, imagePath)
                                }
                                pendingImageUri?.let { uri ->
                                    finalImagePath = withContext(Dispatchers.IO) {
                                        QuestionImageStorage.importFromUri(
                                            context,
                                            stableQuestionId,
                                            uri
                                        )
                                    }
                                    if (finalImagePath == null) {
                                        error = "No se pudo guardar la imagen"
                                        return@launch
                                    }
                                }
                                val question = Question(
                                    id = stableQuestionId,
                                    prompt = prompt.trim(),
                                    type = type,
                                    options = options,
                                    correctIndex = correctIndex,
                                    imagePath = finalImagePath
                                )
                                container.quizRepository.upsertQuestion(courseId, question)
                                onDone()
                            }
                        }
                    }
                },
                containerColor = CseSecondary
            )
            Spacer(modifier = Modifier.height(8.dp))
            SecondaryFlatButton(text = "Cancelar", onClick = onBack)
        }
    }
}

@Composable
private fun FormCard(content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CseWhite)
            .border(1.dp, CseOutlineVariant, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        content()
    }
}

@Composable
private fun OptionRow(
    label: String,
    selected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(CseBackground)
            .border(
                width = if (selected) 2.dp else 1.dp,
                color = if (selected) CseSecondary else CseOutlineVariant,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onSelect)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CorrectRadio(selected = selected)
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            color = CsePrimary
        )
        if (selected) {
            Text(
                text = "CORRECTA",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = CseOnSecondaryContainer,
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(CseSecondaryContainer)
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            )
        }
    }
}

@Composable
private fun OptionEditableRow(
    index: Int,
    value: String,
    selected: Boolean,
    onValueChange: (String) -> Unit,
    onSelect: () -> Unit,
    fieldColors: androidx.compose.material3.TextFieldColors
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(CseBackground)
            .border(
                width = if (selected) 2.dp else 1.dp,
                color = if (selected) CseSecondary else CseOutlineVariant,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .padding(4.dp)
                .clickable(onClick = onSelect),
            contentAlignment = Alignment.Center
        ) {
            CorrectRadio(selected = selected)
        }
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text("Opción ${index + 1}") },
            modifier = Modifier.weight(1f),
            singleLine = true,
            colors = fieldColors,
            shape = RoundedCornerShape(8.dp)
        )
        if (selected) {
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "OK",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = CseOnSecondaryContainer,
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(CseSecondaryContainer)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }
    }
}

@Composable
private fun CorrectRadio(selected: Boolean) {
    Box(
        modifier = Modifier
            .size(22.dp)
            .clip(CircleShape)
            .border(
                width = 2.dp,
                color = if (selected) CseSecondary else CseOutlineVariant,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        if (selected) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(CseSecondary)
            )
        }
    }
}
