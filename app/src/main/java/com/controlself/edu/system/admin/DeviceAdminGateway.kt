package com.controlself.edu.system.admin

import android.content.ComponentName
import android.content.Context
import android.content.Intent

/**
 * Device Admin / Owner (PRP-13).
 */
interface DeviceAdminGateway {
    fun isAdminActive(): Boolean

    /** true si la app es Device Owner (MDM / prueba adb). */
    fun isDeviceOwner(): Boolean

    fun createEnableAdminIntent(): Intent

    /**
     * Desactiva Device Admin. Debe llamarse solo tras verificar la contraseña admin.
     * @return false si no estaba activo o no se pudo quitar.
     */
    fun disableAdmin(): Boolean

    fun adminComponent(): ComponentName
}
