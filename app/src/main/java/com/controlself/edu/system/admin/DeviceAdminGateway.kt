package com.controlself.edu.system.admin

/**
 * Device Admin / Owner (PRP-13). Solo contrato en esta fase.
 */
interface DeviceAdminGateway {
    fun isAdminActive(): Boolean
}
