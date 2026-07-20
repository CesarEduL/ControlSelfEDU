package com.controlself.edu.ui.screens.student.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.controlself.edu.domain.repository.ScreenTimeRepository
import com.controlself.edu.ui.theme.CseDanger
import com.controlself.edu.ui.theme.CseOnSurfaceVariant
import com.controlself.edu.ui.theme.CseOutlineVariant
import com.controlself.edu.ui.theme.CseSecondary
import com.controlself.edu.ui.theme.CseSurfaceContainer
import com.controlself.edu.ui.theme.CseWarning
import com.controlself.edu.ui.theme.CseWhite

@Composable
fun ScreenTimeCard(
    minutesUsed: Int,
    limitMinutes: Int = ScreenTimeRepository.DAILY_LIMIT_MINUTES,
    modifier: Modifier = Modifier
) {
    val progress = (minutesUsed.toFloat() / limitMinutes).coerceIn(0f, 1f)
    val barColor by animateColorAsState(
        targetValue = when {
            minutesUsed >= limitMinutes -> CseDanger
            minutesUsed >= 20 -> CseWarning
            else -> CseSecondary
        },
        label = "screenTimeBar"
    )
    val remaining = (limitMinutes - minutesUsed).coerceAtLeast(0)

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = CseWhite,
        border = BorderStroke(1.dp, CseOutlineVariant),
        shadowElevation = 0.dp
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        Icons.Outlined.Schedule,
                        contentDescription = null,
                        tint = CseSecondary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Tiempo de pantalla",
                        style = MaterialTheme.typography.labelLarge,
                        color = CseOnSurfaceVariant,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = "$minutesUsed/$limitMinutes min",
                    style = MaterialTheme.typography.headlineMedium,
                    color = CseSecondary,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp),
                color = barColor,
                trackColor = CseSurfaceContainer,
                strokeCap = StrokeCap.Round
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (remaining > 0) {
                    "Te quedan $remaining minutos de entretenimiento."
                } else {
                    "Tiempo agotado — completa una lección para continuar."
                },
                style = MaterialTheme.typography.labelMedium,
                color = CseOnSurfaceVariant
            )
        }
    }
}
