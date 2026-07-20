package com.controlself.edu.ui.screens.quiz

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.controlself.edu.di.LocalAppContainer
import com.controlself.edu.domain.model.quiz.QuizAttempt
import com.controlself.edu.ui.preview.PreviewSamples
import com.controlself.edu.ui.theme.ControlSelfEDUTheme
import com.controlself.edu.ui.theme.CseBackground
import com.controlself.edu.ui.theme.CseError
import com.controlself.edu.ui.theme.CseErrorContainer
import com.controlself.edu.ui.theme.CseOnSurfaceVariant
import com.controlself.edu.ui.theme.CseOutlineVariant
import com.controlself.edu.ui.theme.CsePrimary
import com.controlself.edu.ui.theme.CseSecondary
import com.controlself.edu.ui.theme.CseSecondaryContainer
import com.controlself.edu.ui.theme.CseWhite

/**
 * Detalle de aciertos/errores del intento (PRP-08).
 */
@Composable
fun QuizReviewScreen(
    attemptId: String,
    onBack: () -> Unit
) {
    val attempt = LocalAppContainer.current.quizAttemptRepository.getById(attemptId)
    QuizReviewContent(attempt = attempt, onBack = onBack)
}

@Composable
private fun QuizReviewContent(
    attempt: QuizAttempt?,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CseBackground)
    ) {
        IconButton(onClick = onBack, modifier = Modifier.padding(8.dp)) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver",
                tint = CsePrimary
            )
        }

        if (attempt == null) {
            Text(
                text = "Intento no encontrado",
                color = CseError,
                modifier = Modifier.padding(24.dp)
            )
            return
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(bottom = 24.dp)
        ) {
            Text(
                text = "Resultados",
                style = MaterialTheme.typography.headlineLarge,
                color = CsePrimary,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${attempt.courseTitle} · ${attempt.correctCount}/${attempt.total} correctas",
                style = MaterialTheme.typography.bodyMedium,
                color = CseOnSurfaceVariant
            )
            Spacer(modifier = Modifier.height(20.dp))

            attempt.answers.forEachIndexed { index, answer ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(CseWhite)
                        .border(1.dp, CseOutlineVariant, RoundedCornerShape(16.dp))
                        .padding(14.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(
                                if (answer.isCorrect) CseSecondaryContainer else CseErrorContainer
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (answer.isCorrect) Icons.Filled.Check else Icons.Filled.Close,
                            contentDescription = if (answer.isCorrect) "Correcta" else "Incorrecta",
                            tint = if (answer.isCorrect) CseSecondary else CseError,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "${index + 1}. ${answer.prompt}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = CsePrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Tu respuesta: ${answer.userAnswerLabel}",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (answer.isCorrect) CseSecondary else CseError
                        )
                        if (!answer.isCorrect) {
                            Text(
                                text = "Correcta: ${answer.correctAnswerLabel}",
                                style = MaterialTheme.typography.bodySmall,
                                color = CseSecondary,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun QuizReviewScreenPreview() {
    ControlSelfEDUTheme {
        QuizReviewContent(
            attempt = PreviewSamples.quizAttemptFailed,
            onBack = {}
        )
    }
}
