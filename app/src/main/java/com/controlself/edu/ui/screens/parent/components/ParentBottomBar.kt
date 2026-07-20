package com.controlself.edu.ui.screens.parent.components

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

enum class ParentTab(val label: String) {
    HOME("Inicio"),
    COURSES("Cursos"),
    STATS("Stats"),
    PROFILE("Perfil")
}

@Composable
fun ParentBottomBar(
    selected: ParentTab,
    onSelect: (ParentTab) -> Unit,
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
            ParentTab.entries.forEach { tab ->
                val active = tab == selected
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .then(
                            if (active) Modifier.background(CseSecondaryContainer) else Modifier
                        )
                        .clickable { onSelect(tab) }
                        .padding(horizontal = 14.dp, vertical = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = tabIcon(tab, active),
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

private fun tabIcon(tab: ParentTab, filled: Boolean): ImageVector = when (tab) {
    ParentTab.HOME -> if (filled) Icons.Filled.Home else Icons.Outlined.Home
    ParentTab.COURSES -> if (filled) Icons.Filled.School else Icons.Outlined.School
    ParentTab.STATS -> Icons.Outlined.Insights
    ParentTab.PROFILE -> if (filled) Icons.Filled.Person else Icons.Outlined.Person
}
