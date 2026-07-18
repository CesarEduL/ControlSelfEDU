package com.controlself.edu.system.admin

class NoOpDeviceAdminGateway : DeviceAdminGateway {
    override fun isAdminActive(): Boolean = false
}
