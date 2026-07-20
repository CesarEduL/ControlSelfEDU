package com.controlself.edu.ui.screens.lock

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.controlself.edu.ui.components.PrimaryFlatButton
import com.controlself.edu.ui.theme.ControlSelfEDUTheme
import com.controlself.edu.ui.theme.CseBackground
import com.controlself.edu.ui.theme.CseOnSecondary
import com.controlself.edu.ui.theme.CseOnSurfaceVariant
import com.controlself.edu.ui.theme.CseOutline
import com.controlself.edu.ui.theme.CseOutlineVariant
import com.controlself.edu.ui.theme.CsePrimary
import com.controlself.edu.ui.theme.CseSecondary
import com.controlself.edu.ui.theme.CseWhite

@Composable
fun LockScreen(onStartLesson: () -> Unit) {
    val breath = rememberInfiniteTransition(label = "breath")
    val scale by breath.animateFloat(
        initialValue = 1f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CseBackground)
    ) {
        Box(
            modifier = Modifier
                .size(220.dp)
                .offset(x = (-40).dp, y = (-60).dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            CsePrimary.copy(alpha = 0.12f),
                            CseBackground.copy(alpha = 0f)
                        )
                    ),
                    CircleShape
                )
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(240.dp)
                .offset(x = 50.dp, y = 40.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            CseSecondary.copy(alpha = 0.15f),
                            CseBackground.copy(alpha = 0f)
                        )
                    ),
                    CircleShape
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .padding(top = 72.dp, bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "¡Tiempo de estudio!",
                style = MaterialTheme.typography.headlineLarge,
                color = CsePrimary,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Concéntrate en tu aprendizaje hoy.",
                style = MaterialTheme.typography.bodyMedium,
                color = CseOnSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(40.dp))

            Box(contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .size(220.dp)
                        .clip(CircleShape)
                        .background(CsePrimary.copy(alpha = 0.05f))
                )
                Box(
                    modifier = Modifier.scale(scale),
                    contentAlignment = Alignment.TopEnd
                ) {
                    Box(
                        modifier = Modifier
                            .size(160.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(CseWhite)
                            .border(2.dp, CseOutlineVariant, RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Filled.SmartToy,
                            contentDescription = null,
                            tint = CsePrimary,
                            modifier = Modifier.size(88.dp)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .offset(x = 12.dp, y = (-10).dp)
                            .clip(RoundedCornerShape(50))
                            .background(CseSecondary)
                            .padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Filled.Bolt,
                            contentDescription = null,
                            tint = CseOnSecondary,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = " EN LÍNEA",
                            style = MaterialTheme.typography.labelMedium,
                            color = CseOnSecondary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            PrimaryFlatButton(text = "Comenzar lección", onClick = onStartLesson)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Tu progreso se guardará automáticamente al finalizar.",
                style = MaterialTheme.typography.labelMedium,
                color = CseOutline,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "MVP: bloqueo dentro de ControlSelf EDU. Device Admin refuerza la protección.",
                style = MaterialTheme.typography.labelSmall,
                color = CseOutline.copy(alpha = 0.8f),
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
