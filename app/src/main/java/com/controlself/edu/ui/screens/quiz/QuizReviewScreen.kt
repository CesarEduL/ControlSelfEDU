package com.controlself.edu.ui.screens.quiz

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.controlself.edu.di.LocalAppContainer
import com.controlself.edu.ui.theme.CseBlue
import com.controlself.edu.ui.theme.CseDanger
import com.controlself.edu.ui.theme.CseGreen
import com.controlself.edu.ui.theme.CseMuted
import com.controlself.edu.ui.theme.CseSurface

/**
 * Detalle de aciertos/errores del intento (PRP-08).
 */
@Composable
fun QuizReviewScreen(
    attemptId: String,
    onBack: () -> Unit
) {
    val attempt = LocalAppContainer.current.quizAttemptRepository.getById(attemptId)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CseSurface)
    ) {
        IconButton(onClick = onBack, modifier = Modifier.padding(8.dp)) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
        }

        if (attempt == null) {
            Text(
                text = "Intento no encontrado",
                color = CseDanger,
                modifier = Modifier.padding(24.dp)
            )
            return
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(bottom = 24.dp)
        ) {
            Text(
                text = "Resultados",
                style = MaterialTheme.typography.headlineSmall,
                color = CseBlue,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${attempt.courseTitle} · ${attempt.correctCount}/${attempt.total} correctas",
                style = MaterialTheme.typography.bodyMedium,
                color = CseMuted
            )
            Spacer(modifier = Modifier.height(20.dp))

            attempt.answers.forEachIndexed { index, answer ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 14.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = if (answer.isCorrect) Icons.Filled.Check else Icons.Filled.Close,
                        contentDescription = if (answer.isCorrect) "Correcta" else "Incorrecta",
                        tint = if (answer.isCorrect) CseGreen else CseDanger
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "${index + 1}. ${answer.prompt}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Tu respuesta: ${answer.userAnswerLabel}",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (answer.isCorrect) CseGreen else CseDanger
                        )
                        if (!answer.isCorrect) {
                            Text(
                                text = "Correcta: ${answer.correctAnswerLabel}",
                                style = MaterialTheme.typography.bodySmall,
                                color = CseGreen,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}
