package com.controlself.edu.ui.screens.lock

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.controlself.edu.ui.theme.ControlSelfEDUTheme
import com.controlself.edu.ui.theme.CseBlue
import com.controlself.edu.ui.theme.CseBlueDark
import com.controlself.edu.ui.theme.CseGreen
import com.controlself.edu.ui.theme.CseWhite

@Composable
fun LockScreen(
    onStartLesson: () -> Unit
) {
    BackHandler {
        // No desbloquea ni vuelve al home.
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(CseBlue, CseBlueDark))
            )
            .padding(28.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(88.dp)
                    .clip(CircleShape)
                    .background(CseWhite),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(CseGreen)
                )
            }
            Spacer(modifier = Modifier.height(28.dp))
            Text(
                text = "¡Tiempo cumplido!",
                style = MaterialTheme.typography.displayLarge,
                color = CseWhite,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Has utilizado tus 30 minutos de entretenimiento.\n" +
                    "Para seguir usando tus aplicaciones necesitas completar una lección.",
                style = MaterialTheme.typography.bodyLarge,
                color = CseWhite.copy(alpha = 0.92f),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(36.dp))
            Button(
                onClick = onStartLesson,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CseGreen,
                    contentColor = CseWhite
                )
            ) {
                Text(
                    text = "Comenzar lección",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "MVP: este bloqueo aplica dentro de ControlSelf EDU. " +
                    "El bloqueo forzado de otras apps en el sistema depende del Device Admin " +
                    "y, en entornos escolares, de Device Owner.",
                style = MaterialTheme.typography.bodySmall,
                color = CseWhite.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LockScreenPreview() {
    ControlSelfEDUTheme {
        LockScreen(onStartLesson = {})
    }
}
