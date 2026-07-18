package com.controlself.edu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.controlself.edu.di.LocalAppContainer
import com.controlself.edu.navigation.ControlSelfNavHost
import com.controlself.edu.ui.theme.ControlSelfEDUTheme
import com.controlself.edu.ui.theme.CseWhite

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as ControlSelfApplication

        setContent {
            CompositionLocalProvider(LocalAppContainer provides app.container) {
                ControlSelfEDUTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = CseWhite
                    ) {
                        ControlSelfNavHost()
                    }
                }
            }
        }
    }
}
