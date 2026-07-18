package com.controlself.edu

import android.app.Application
import com.controlself.edu.di.AppContainer

class ControlSelfApplication : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
