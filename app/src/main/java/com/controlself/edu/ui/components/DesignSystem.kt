package com.controlself.edu.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.controlself.edu.ui.theme.CseOutlineVariant
import com.controlself.edu.ui.theme.CsePrimary
import com.controlself.edu.ui.theme.CseSecondary
import com.controlself.edu.ui.theme.CseWhite

/** Card plana: blanco + borde sutil, sin sombra (design system). */
@Composable
fun FlatCard(
    modifier: Modifier = Modifier,
    accentColor: Color? = null,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CseWhite)
            .then(
                if (accentColor != null) {
                    Modifier.border(
                        BorderStroke(1.dp, CseOutlineVariant),
                        RoundedCornerShape(16.dp)
                    )
                } else {
                    Modifier.border(
                        BorderStroke(1.dp, CseOutlineVariant),
                        RoundedCornerShape(16.dp)
                    )
                }
            )
            .padding(16.dp)
    ) {
        content()
    }
}

@Composable
fun PrimaryFlatButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    containerColor: Color = CsePrimary
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = CseWhite
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SecondaryFlatButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(2.dp, CsePrimary),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = CsePrimary)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}

/** Color de acción primario según rol (design: student green / parent navy / teacher teal-ish). */
fun roleActionColor(isStudent: Boolean, isParent: Boolean): Color = when {
    isStudent -> CseSecondary
    isParent -> CsePrimary
    else -> CseOnPrimaryFixedVariantCompat
}

private val CseOnPrimaryFixedVariantCompat = Color(0xFF354862)
