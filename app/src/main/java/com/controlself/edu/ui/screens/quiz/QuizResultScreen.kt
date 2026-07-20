package com.controlself.edu.ui.screens.quiz

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.SmartDisplay
import androidx.compose.material.icons.outlined.SportsEsports
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.controlself.edu.di.LocalAppContainer
import com.controlself.edu.domain.model.quiz.QuizAttempt
import com.controlself.edu.ui.components.PrimaryFlatButton
import com.controlself.edu.ui.components.SecondaryFlatButton
import com.controlself.edu.ui.preview.PreviewSamples
import com.controlself.edu.ui.theme.ControlSelfEDUTheme
import com.controlself.edu.ui.theme.CseBackground
import com.controlself.edu.ui.theme.CseError
import com.controlself.edu.ui.theme.CseErrorContainer
import com.controlself.edu.ui.theme.CseOnSecondaryContainer
import com.controlself.edu.ui.theme.CseOnSurfaceVariant
import com.controlself.edu.ui.theme.CseOutlineVariant
import com.controlself.edu.ui.theme.CsePrimary
import com.controlself.edu.ui.theme.CseSecondary
import com.controlself.edu.ui.theme.CseSecondaryContainer
import com.controlself.edu.ui.theme.CseSurfaceContainer
import com.controlself.edu.ui.theme.CseSurfaceHighest
import com.controlself.edu.ui.theme.CseSurfaceLow
import com.controlself.edu.ui.theme.CseWhite

@Composable
fun QuizResultScreen(
    attemptId: String,
    onReview: (attemptId: String) -> Unit,
    onRetry: (courseId: String) -> Unit,
    onHome: () -> Unit,
    onBackToLock: () -> Unit
) {
    val attempt = LocalAppContainer.current.quizAttemptRepository.getById(attemptId)
    QuizResultContent(
        attempt = attempt,
        onReview = onReview,
        onRetry = onRetry,
        onHome = onHome,
        onBackToLock = onBackToLock
    )
}

@Composable
private fun QuizResultContent(
    attempt: QuizAttempt?,
    onReview: (attemptId: String) -> Unit,
    onRetry: (courseId: String) -> Unit,
    onHome: () -> Unit,
    onBackToLock: () -> Unit
) {
    BackHandler {
        if (attempt?.passed == true) onHome() else onBackToLock()
    }

    if (attempt == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(CseBackground)
                .padding(24.dp)
        ) {
            Text("Intento no encontrado", color = CseError)
            Spacer(modifier = Modifier.height(16.dp))
            PrimaryFlatButton(text = "Volver", onClick = onBackToLock)
        }
        return
    }

    val wrongCount = attempt.answers.count { !it.isCorrect }
    val progress = attempt.correctCount.toFloat() / attempt.total

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CseBackground)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (attempt.passed) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(CseSecondaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.CheckCircle,
                    contentDescription = null,
                    tint = CseOnSecondaryContainer,
                    modifier = Modifier.size(56.dp)
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "¡Excelente trabajo!",
                style = MaterialTheme.typography.displayMedium,
                color = CsePrimary,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Has completado el desafío de hoy",
                style = MaterialTheme.typography.bodyLarge,
                color = CseOnSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(CseSurfaceHighest)
                    .padding(horizontal = 24.dp, vertical = 10.dp)
            ) {
                Text(
                    text = "${attempt.correctCount}/${attempt.total}",
                    style = MaterialTheme.typography.headlineLarge,
                    color = CsePrimary,
                    fontWeight = FontWeight.ExtraBold
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = CseSecondary,
                trackColor = CseSurfaceContainer,
                strokeCap = StrokeCap.Round
            )
            Spacer(modifier = Modifier.height(28.dp))
            Text(
                text = "APPS DESBLOQUEADAS",
                style = MaterialTheme.typography.labelLarge,
                color = CseOnSurfaceVariant,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                UnlockedAppChip("Video", Icons.Outlined.SmartDisplay, Modifier.weight(1f))
                UnlockedAppChip("Juegos", Icons.Outlined.SportsEsports, Modifier.weight(1f))
                UnlockedAppChip("Social", Icons.Outlined.Groups, Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Puedes usar redes y juegos el resto del día.",
                style = MaterialTheme.typography.bodyMedium,
                color = CseOnSurfaceVariant
            )
            Spacer(modifier = Modifier.height(28.dp))
            SecondaryFlatButton(text = "Ver resultados", onClick = { onReview(attempt.id) })
            Spacer(modifier = Modifier.height(12.dp))
            PrimaryFlatButton(text = "Volver al inicio", onClick = onHome)
        } else {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(CseErrorContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "!",
                    style = MaterialTheme.typography.displayMedium,
                    color = CseError,
                    fontWeight = FontWeight.ExtraBold
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "¡Sigue intentándolo!",
                style = MaterialTheme.typography.displayMedium,
                color = CsePrimary,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Aún no alcanzas el mínimo para desbloquear",
                style = MaterialTheme.typography.bodyLarge,
                color = CseOnSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(CseErrorContainer)
                    .padding(horizontal = 24.dp, vertical = 10.dp)
            ) {
                Text(
                    text = "${attempt.correctCount}/${attempt.total}",
                    style = MaterialTheme.typography.headlineLarge,
                    color = CseError,
                    fontWeight = FontWeight.ExtraBold
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = CseError,
                trackColor = CseSurfaceContainer,
                strokeCap = StrokeCap.Round
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Necesitas al menos ${QuizAttempt.PASS_THRESHOLD} correctas. " +
                    "Erraste $wrongCount pregunta${if (wrongCount == 1) "" else "s"}. " +
                    "Revisa las respuestas y vuelve a intentar.",
                style = MaterialTheme.typography.bodyLarge,
                color = CseOnSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(28.dp))
            SecondaryFlatButton(text = "Ver resultados", onClick = { onReview(attempt.id) })
            Spacer(modifier = Modifier.height(12.dp))
            PrimaryFlatButton(text = "Reintentar", onClick = { onRetry(attempt.courseId) })
            Spacer(modifier = Modifier.height(12.dp))
            SecondaryFlatButton(text = "Volver al bloqueo", onClick = onBackToLock)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = attempt.courseTitle,
            style = MaterialTheme.typography.labelMedium,
            color = CseOnSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun UnlockedAppChip(
    label: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(CseSurfaceLow)
            .border(1.dp, CseOutlineVariant, RoundedCornerShape(12.dp))
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(icon, contentDescription = null, tint = CsePrimary, modifier = Modifier.size(32.dp))
        Spacer(modifier = Modifier.height(6.dp))
        Text(label, style = MaterialTheme.typography.labelMedium, color = CseOnSurfaceVariant)
    }
}

@Preview(showBackground = true, name = "Resultado aprobado")
@Composable
private fun QuizResultScreenPassedPreview() {
    ControlSelfEDUTheme {
        QuizResultContent(
            attempt = PreviewSamples.quizAttemptPassed,
            onReview = {},
            onRetry = {},
            onHome = {},
            onBackToLock = {}
        )
    }
}

@Preview(showBackground = true, name = "Resultado reprobado")
@Composable
private fun QuizResultScreenFailedPreview() {
    ControlSelfEDUTheme {
        QuizResultContent(
            attempt = PreviewSamples.quizAttemptFailed,
            onReview = {},
            onRetry = {},
            onHome = {},
            onBackToLock = {}
        )
    }
}
