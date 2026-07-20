package com.controlself.edu.ui.screens.student.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Insights
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.controlself.edu.ui.theme.CseOnSecondaryContainer
import com.controlself.edu.ui.theme.CseOnSurfaceVariant
import com.controlself.edu.ui.theme.CseOutlineVariant
import com.controlself.edu.ui.theme.CseSecondaryContainer
import com.controlself.edu.ui.theme.CseWhite

enum class StudentTab(val label: String) {
    HOME("Inicio"),
    COURSES("Cursos"),
    STATS("Stats"),
    PROFILE("Perfil")
}

/** Bottom nav flat: pill verde en el tab activo. */
@Composable
fun StudentBottomBar(
    selected: StudentTab,
    onSelect: (StudentTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        HorizontalDivider(color = CseOutlineVariant, thickness = 1.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(CseWhite)
                .padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StudentTab.entries.forEach { tab ->
                val active = tab == selected
                val icon = tabIcon(tab, filled = active)
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .then(
                            if (active) {
                                Modifier.background(CseSecondaryContainer)
                            } else {
                                Modifier
                            }
                        )
                        .clickable { onSelect(tab) }
                        .padding(horizontal = 14.dp, vertical = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = tab.label,
                        tint = if (active) CseOnSecondaryContainer else CseOnSurfaceVariant
                    )
                    Text(
                        text = tab.label,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (active) FontWeight.Bold else FontWeight.SemiBold,
                        color = if (active) CseOnSecondaryContainer else CseOnSurfaceVariant
                    )
                }
            }
        }
    }
}

private fun tabIcon(tab: StudentTab, filled: Boolean): ImageVector = when (tab) {
    StudentTab.HOME -> if (filled) Icons.Filled.Home else Icons.Outlined.Home
    StudentTab.COURSES -> if (filled) Icons.Filled.School else Icons.Outlined.School
    StudentTab.STATS -> Icons.Outlined.Insights
    StudentTab.PROFILE -> if (filled) Icons.Filled.Person else Icons.Outlined.Person
}
