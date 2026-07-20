package com.controlself.edu.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FamilyRestroom
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.outlined.Person
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
import com.controlself.edu.domain.model.UserRole
import com.controlself.edu.ui.theme.CseOnSurfaceVariant
import com.controlself.edu.ui.theme.CseOutlineVariant
import com.controlself.edu.ui.theme.CsePrimary
import com.controlself.edu.ui.theme.CseSurfaceLow
import com.controlself.edu.ui.theme.CseWhite

@Composable
fun RoleSelector(
    selected: UserRole,
    onSelected: (UserRole) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Selecciona tu perfil",
            style = MaterialTheme.typography.labelLarge,
            color = CseOnSurfaceVariant,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(CseSurfaceLow)
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            RoleChip(
                label = "Estudiante",
                icon = Icons.Filled.School,
                selected = selected == UserRole.STUDENT,
                onClick = { onSelected(UserRole.STUDENT) },
                modifier = Modifier.weight(1f)
            )
            RoleChip(
                label = "Padre",
                icon = Icons.Filled.FamilyRestroom,
                selected = selected == UserRole.PARENT,
                onClick = { onSelected(UserRole.PARENT) },
                modifier = Modifier.weight(1f)
            )
            RoleChip(
                label = "Docente",
                icon = Icons.Outlined.Person,
                selected = selected == UserRole.TEACHER,
                onClick = { onSelected(UserRole.TEACHER) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun RoleChip(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (selected) CseWhite else androidx.compose.ui.graphics.Color.Transparent)
            .then(
                if (selected) {
                    Modifier.border(1.dp, CseOutlineVariant, RoundedCornerShape(10.dp))
                } else {
                    Modifier
                }
            )
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (selected) CsePrimary else CseOnSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = if (selected) CsePrimary else CseOnSurfaceVariant,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
