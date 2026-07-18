package com.controlself.edu.ui.screens.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.controlself.edu.ui.theme.CseBlue
import com.controlself.edu.ui.theme.CseMuted
import com.controlself.edu.ui.theme.CseSurface

/**
 * Stub de panel por rol hasta PRP-04 / 11 / 12.
 */
@Composable
fun RoleHomeStubScreen(
    roleTitle: String,
    upcomingPrp: String
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
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Panel en construcción.\nSe implementa en $upcomingPrp.",
            style = MaterialTheme.typography.bodyMedium,
            color = CseMuted,
            textAlign = TextAlign.Center
        )
    }
}
