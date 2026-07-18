package com.controlself.edu.di

import androidx.compose.runtime.staticCompositionLocalOf

val LocalAppContainer = staticCompositionLocalOf<AppContainer> {
    error("AppContainer no provisto. Envuelve la UI con CompositionLocalProvider.")
}
