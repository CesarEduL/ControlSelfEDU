package com.controlself.edu.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val ControlSelfColorScheme = lightColorScheme(
    primary = CseBlue,
    onPrimary = CseWhite,
    primaryContainer = CseBlueLight,
    onPrimaryContainer = CseBlueDark,
    secondary = CseGreen,
    onSecondary = CseWhite,
    secondaryContainer = CseGreenLight,
    onSecondaryContainer = CseGreen,
    background = CseWhite,
    onBackground = CseOnSurface,
    surface = CseSurface,
    onSurface = CseOnSurface,
    onSurfaceVariant = CseMuted,
    error = CseDanger,
    onError = CseWhite
)

@Composable
fun ControlSelfEDUTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ControlSelfColorScheme,
        typography = ControlSelfTypography,
        content = content
    )
}
