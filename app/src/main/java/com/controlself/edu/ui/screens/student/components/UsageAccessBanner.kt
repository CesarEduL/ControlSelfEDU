package com.controlself.edu.ui.screens.student.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.controlself.edu.ui.theme.CseBlue
import com.controlself.edu.ui.theme.CseMuted

@Composable
fun UsageAccessBanner(
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = CseBlue.copy(alpha = 0.08f),
        shadowElevation = 0.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Activa el monitoreo de uso",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = CseBlue
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Para contar el tiempo en redes y juegos necesitamos el permiso " +
                    "“Acceso al uso”. Actívalo para ControlSelf EDU en Ajustes.",
                style = MaterialTheme.typography.bodyMedium,
                color = CseMuted
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = onOpenSettings, modifier = Modifier.fillMaxWidth()) {
                Text("Abrir ajustes de uso")
            }
        }
    }
}
