package com.controlself.edu.system.admin

/** Stub para tests / previews. */
class NoOpDeviceAdminGateway : DeviceAdminGateway {
    override fun isAdminActive(): Boolean = false
    override fun isDeviceOwner(): Boolean = false
    override fun createEnableAdminIntent(): android.content.Intent =
        android.content.Intent()
    override fun disableAdmin(): Boolean = false
    override fun adminComponent(): android.content.ComponentName =
        android.content.ComponentName("com.controlself.edu", "NoOp")
}
