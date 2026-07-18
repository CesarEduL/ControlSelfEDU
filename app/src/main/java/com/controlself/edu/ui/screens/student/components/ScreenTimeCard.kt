package com.controlself.edu.ui.screens.student.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.controlself.edu.domain.repository.ScreenTimeRepository
import com.controlself.edu.ui.theme.CseDanger
import com.controlself.edu.ui.theme.CseGreen
import com.controlself.edu.ui.theme.CseMuted
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
            else -> CseGreen
        },
        label = "screenTimeBar"
    )
    val remaining = (limitMinutes - minutesUsed).coerceAtLeast(0)

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = CseWhite,
        tonalElevation = 1.dp,
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Tiempo de hoy",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$minutesUsed minutos de $limitMinutes disponibles",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = if (remaining > 0) {
                    "$remaining min restantes"
                } else {
                    "Tiempo agotado"
                },
                style = MaterialTheme.typography.bodySmall,
                color = CseMuted
            )
            Spacer(modifier = Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp),
                color = barColor,
                trackColor = barColor.copy(alpha = 0.2f),
                strokeCap = StrokeCap.Round
            )
        }
    }
}
