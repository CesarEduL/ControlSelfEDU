package com.controlself.edu.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val ControlSelfColorScheme = lightColorScheme(
    primary = CsePrimary,
    onPrimary = CseOnPrimary,
    primaryContainer = CsePrimaryContainer,
    onPrimaryContainer = CseOnPrimaryContainer,
    secondary = CseSecondary,
    onSecondary = CseOnSecondary,
    secondaryContainer = CseSecondaryContainer,
    onSecondaryContainer = CseOnSecondaryContainer,
    tertiary = CseTertiary,
    tertiaryContainer = CseTertiaryContainer,
    background = CseBackground,
    onBackground = CseOnSurface,
    surface = CseWhite,
    onSurface = CseOnSurface,
    surfaceVariant = CseSurfaceHighest,
    onSurfaceVariant = CseOnSurfaceVariant,
    outline = CseOutline,
    outlineVariant = CseOutlineVariant,
    error = CseError,
    onError = CseOnError,
    errorContainer = CseErrorContainer
)

private val ControlSelfShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(24.dp)
)

@Composable
fun ControlSelfEDUTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ControlSelfColorScheme,
        typography = ControlSelfTypography,
        shapes = ControlSelfShapes,
        content = content
    )
}
