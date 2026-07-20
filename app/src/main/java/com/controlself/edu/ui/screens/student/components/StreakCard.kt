package com.controlself.edu.ui.screens.student.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.controlself.edu.domain.model.motivation.AchievementBadge
import com.controlself.edu.ui.theme.CseOnSurfaceVariant
import com.controlself.edu.ui.theme.CseOutlineVariant
import com.controlself.edu.ui.theme.CsePrimary
import com.controlself.edu.ui.theme.CsePrimaryFixed
import com.controlself.edu.ui.theme.CseSecondary
import com.controlself.edu.ui.theme.CseWhite
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StreakCard(
    streakDays: Int,
    badges: List<AchievementBadge>,
    modifier: Modifier = Modifier
) {
    val unlockedHistory = badges
        .filter { it.unlocked && it.unlockedAtMillis != null && it.unlockedAtMillis > 0L }
        .sortedByDescending { it.unlockedAtMillis }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = CseWhite,
        border = BorderStroke(1.dp, CseOutlineVariant),
        shadowElevation = 0.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Mi racha",
                style = MaterialTheme.typography.labelLarge,
                color = CsePrimary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (streakDays > 0) {
                    "$streakDays días seguidos aprendiendo"
                } else {
                    "Sin racha activa — completa una evaluación hoy"
                },
                style = MaterialTheme.typography.bodyLarge,
                color = if (streakDays > 0) CseSecondary else CseOnSurfaceVariant,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Insignias",
                style = MaterialTheme.typography.labelMedium,
                color = CseOnSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                badges.forEach { badge ->
                    AssistChip(
                        onClick = {},
                        enabled = badge.unlocked,
                        label = { Text(badge.title) },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = if (badge.unlocked) {
                                CsePrimaryFixed
                            } else {
                                CseOnSurfaceVariant.copy(alpha = 0.08f)
                            },
                            labelColor = if (badge.unlocked) CsePrimary else CseOnSurfaceVariant,
                            disabledContainerColor = CseOnSurfaceVariant.copy(alpha = 0.08f),
                            disabledLabelColor = CseOnSurfaceVariant
                        )
                    )
                }
            }
            if (unlockedHistory.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Historial de logros",
                    style = MaterialTheme.typography.labelMedium,
                    color = CseOnSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                unlockedHistory.forEach { badge ->
                    Text(
                        text = "${badge.title} · ${formatUnlockDate(badge.unlockedAtMillis!!)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = CseOnSurfaceVariant,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
            }
        }
    }
}

private fun formatUnlockDate(millis: Long): String {
    val formatter = DateTimeFormatter.ofPattern("d MMM yyyy", Locale("es", "PE"))
    return Instant.ofEpochMilli(millis)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
        .format(formatter)
}
