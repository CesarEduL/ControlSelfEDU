package com.controlself.edu.navigation

/**
 * Rutas de navegación de ControlSelf EDU.
 * Se irán ampliando en PRs posteriores (auth, paneles, evaluaciones, etc.).
 */
sealed class Screen(val route: String) {
    data object Welcome : Screen("welcome")
    data object Login : Screen("login")
}

object Routes {
    const val WELCOME = "welcome"
    const val LOGIN = "login"
}
