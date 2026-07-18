package com.controlself.edu.ui.screens.student.components

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
import com.controlself.edu.domain.model.BadgeMock
import com.controlself.edu.ui.theme.CseBlue
import com.controlself.edu.ui.theme.CseGreen
import com.controlself.edu.ui.theme.CseMuted
import com.controlself.edu.ui.theme.CseWhite

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StreakCard(
    streakDays: Int,
    badges: List<BadgeMock>,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = CseWhite,
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Mi racha",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$streakDays días seguidos aprendiendo",
                style = MaterialTheme.typography.bodyLarge,
                color = CseGreen,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Insignias",
                style = MaterialTheme.typography.labelLarge,
                color = CseMuted
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
                                CseBlue.copy(alpha = 0.12f)
                            } else {
                                CseMuted.copy(alpha = 0.12f)
                            },
                            labelColor = if (badge.unlocked) CseBlue else CseMuted,
                            disabledContainerColor = CseMuted.copy(alpha = 0.08f),
                            disabledLabelColor = CseMuted
                        )
                    )
                }
            }
        }
    }
}
