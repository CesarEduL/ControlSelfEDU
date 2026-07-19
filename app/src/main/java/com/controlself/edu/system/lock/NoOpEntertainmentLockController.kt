package com.controlself.edu.system.lock

/** Placeholder de overlay de sistema; el bloqueo UI real navega a `LockScreen` (PRP-06). */
class NoOpEntertainmentLockController : EntertainmentLockController {
    override fun showLockScreen() = Unit
}
