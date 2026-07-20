package com.controlself.edu.ui.screens.quiz

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.controlself.edu.domain.model.Course
import com.controlself.edu.domain.model.quiz.QuizAttempt
import com.controlself.edu.ui.components.PrimaryFlatButton
import com.controlself.edu.ui.theme.ControlSelfEDUTheme
import com.controlself.edu.ui.theme.CseBackground
import com.controlself.edu.ui.theme.CseOnSurfaceVariant
import com.controlself.edu.ui.theme.CseOutlineVariant
import com.controlself.edu.ui.theme.CsePrimary
import com.controlself.edu.ui.theme.CseSecondary
import com.controlself.edu.ui.theme.CseSecondaryContainer
import com.controlself.edu.ui.theme.CseWhite

@Composable
fun CourseIntroScreen(
    course: Course,
    onStartQuiz: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CseBackground)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        IconButton(onClick = onBack) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver",
                tint = CsePrimary
            )
        }
        Text(
            text = course.title,
            style = MaterialTheme.typography.headlineLarge,
            color = CsePrimary,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(CseWhite)
                .border(1.dp, CseOutlineVariant, RoundedCornerShape(16.dp))
                .padding(20.dp)
        ) {
            Text(
                text = "Nivel: ${course.difficulty}",
                style = MaterialTheme.typography.labelLarge,
                color = CseSecondary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(CseSecondaryContainer.copy(alpha = 0.45f))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Esta lección tiene ${QuizAttempt.TOTAL_QUESTIONS} preguntas " +
                    "(opción múltiple y verdadero/falso).",
                style = MaterialTheme.typography.bodyLarge,
                color = CseOnSurfaceVariant
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Necesitas al menos ${QuizAttempt.PASS_THRESHOLD} correctas para desbloquear.",
                style = MaterialTheme.typography.bodyLarge,
                color = CsePrimary,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(24.dp))
            PrimaryFlatButton(
                text = "Comenzar evaluación",
                onClick = onStartQuiz,
                containerColor = CseSecondary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CourseIntroScreenPreview() {
    ControlSelfEDUTheme {
        CourseIntroScreen(
            course = Course.MATH,
            onStartQuiz = {},
            onBack = {}
        )
    }
}
