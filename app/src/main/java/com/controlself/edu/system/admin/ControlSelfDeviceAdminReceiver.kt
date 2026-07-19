package com.controlself.edu.system.admin

import android.app.admin.DeviceAdminReceiver
import android.content.Context
import android.content.Intent

/**
 * Receiver Device Admin (PRP-13).
 * En consumer Android no puede bloquear la desactivación con password;
 * sí muestra un aviso y la app exige password para desactivar desde su UI.
 */
class ControlSelfDeviceAdminReceiver : DeviceAdminReceiver() {

    override fun onEnabled(context: Context, intent: Intent) {
        super.onEnabled(context, intent)
    }

    override fun onDisabled(context: Context, intent: Intent) {
        super.onDisabled(context, intent)
    }

    override fun onDisableRequested(context: Context, intent: Intent): CharSequence {
        return "Protección ControlSelf EDU: un padre o docente debe desactivar " +
            "Device Admin desde la app con la contraseña de administrador."
    }
}
