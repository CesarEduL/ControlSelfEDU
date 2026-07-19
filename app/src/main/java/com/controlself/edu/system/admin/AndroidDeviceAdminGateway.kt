package com.controlself.edu.system.admin

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent

class AndroidDeviceAdminGateway(
    context: Context
) : DeviceAdminGateway {

    private val appContext = context.applicationContext
    private val dpm =
        appContext.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
    private val component = ComponentName(appContext, ControlSelfDeviceAdminReceiver::class.java)

    override fun isAdminActive(): Boolean = dpm.isAdminActive(component)

    override fun isDeviceOwner(): Boolean =
        runCatching { dpm.isDeviceOwnerApp(appContext.packageName) }.getOrDefault(false)

    override fun createEnableAdminIntent(): Intent =
        Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN).apply {
            putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, component)
            putExtra(
                DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                "Activa la protección para dificultar la desinstalación sin un adulto. " +
                    "En dispositivos escolares (Device Owner) el control es mayor."
            )
        }

    override fun disableAdmin(): Boolean {
        if (!isAdminActive()) return false
        return runCatching {
            dpm.removeActiveAdmin(component)
            true
        }.getOrDefault(false)
    }

    override fun adminComponent(): ComponentName = component
}
