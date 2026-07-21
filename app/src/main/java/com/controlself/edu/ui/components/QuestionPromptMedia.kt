package com.controlself.edu.ui.components

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.controlself.edu.data.quiz.QuestionImageStorage
import com.controlself.edu.ui.theme.CseOnSurface
import com.controlself.edu.ui.theme.CseOnSurfaceVariant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun QuestionPromptMedia(
    prompt: String,
    imagePath: String? = null,
    pendingImageUri: Uri? = null,
    promptPlaceholder: String = "El enunciado aparecerá aquí",
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var bitmap by remember { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(imagePath, pendingImageUri) {
        bitmap = withContext(Dispatchers.IO) {
            when {
                pendingImageUri != null -> {
                    context.contentResolver.openInputStream(pendingImageUri)?.use { stream ->
                        BitmapFactory.decodeStream(stream)?.asImageBitmap()
                    }
                }
                !imagePath.isNullOrBlank() -> {
                    val file = QuestionImageStorage.resolveFile(context, imagePath)
                    if (file.exists()) {
                        BitmapFactory.decodeFile(file.absolutePath)?.asImageBitmap()
                    } else {
                        null
                    }
                }
                else -> null
            }
        }
    }

    if (bitmap != null) {
        Image(
            bitmap = bitmap!!,
            contentDescription = "Imagen del enunciado",
            modifier = modifier
                .fillMaxWidth()
                .heightIn(max = 220.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.height(12.dp))
    }

    Text(
        text = prompt.ifBlank { promptPlaceholder },
        style = MaterialTheme.typography.titleMedium,
        color = if (prompt.isBlank()) CseOnSurfaceVariant else CseOnSurface,
        fontWeight = FontWeight.SemiBold,
        modifier = modifier.fillMaxWidth()
    )
}
