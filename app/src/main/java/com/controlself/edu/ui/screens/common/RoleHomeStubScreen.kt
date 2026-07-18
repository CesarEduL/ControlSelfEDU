package com.controlself.edu.ui.screens.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.controlself.edu.ui.theme.ControlSelfEDUTheme
import com.controlself.edu.ui.theme.CseBlue
import com.controlself.edu.ui.theme.CseMuted
import com.controlself.edu.ui.theme.CseSurface

@Composable
fun RoleHomeStubScreen(
    roleTitle: String,
    upcomingPrp: String,
    displayName: String? = null,
    onLogout: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CseSurface)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = roleTitle,
            style = MaterialTheme.typography.headlineMedium,
            color = CseBlue,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        if (!displayName.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Hola, $displayName",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Panel en construcción.\nSe implementa en $upcomingPrp.",
            style = MaterialTheme.typography.bodyMedium,
            color = CseMuted,
            textAlign = TextAlign.Center
        )
        if (onLogout != null) {
            Spacer(modifier = Modifier.height(24.dp))
            OutlinedButton(onClick = onLogout) {
                Text("Cerrar sesión")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RoleHomeStubScreenPreview() {
    ControlSelfEDUTheme {
        RoleHomeStubScreen(
            roleTitle = "Panel docente",
            upcomingPrp = "PRP-11",
            displayName = "María",
            onLogout = {}
        )
    }
}
