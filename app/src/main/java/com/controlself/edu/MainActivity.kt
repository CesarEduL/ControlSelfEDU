package com.controlself.edu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.controlself.edu.di.LocalAppContainer
import com.controlself.edu.navigation.ControlSelfNavHost
import com.controlself.edu.ui.theme.ControlSelfEDUTheme
import com.controlself.edu.ui.theme.CseWhite

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        var keepSplash by mutableStateOf(true)
        splashScreen.setKeepOnScreenCondition { keepSplash }

        enableEdgeToEdge()

        val app = application as ControlSelfApplication

        setContent {
            SideEffect { keepSplash = false }

            CompositionLocalProvider(LocalAppContainer provides app.container) {
                ControlSelfEDUTheme {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .safeDrawingPadding(),
                        color = CseWhite
                    ) {
                        ControlSelfNavHost()
                    }
                }
            }
        }
    }
}
