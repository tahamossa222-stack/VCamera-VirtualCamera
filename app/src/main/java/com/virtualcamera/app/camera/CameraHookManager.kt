package com.virtualcamera.app.camera

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CameraHookManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val virtualCameraEngine: VirtualCameraEngine,
    private val surfaceManager: SurfaceManager
) {
    private var isHookInstalled = false

    fun installHooks() {
        if (isHookInstalled) return
        isHookInstalled = true
    }

    fun uninstallHooks() {
        if (!isHookInstalled) return
        isHookInstalled = false
        surfaceManager.releaseAll()
        virtualCameraEngine.stop()
    }

    fun isHooksInstalled(): Boolean = isHookInstalled

    fun getAvailableCameras(): List<CameraInfo> {
        return virtualCameraEngine.getCameraIds().map { id ->
            val characteristics = virtualCameraEngine.getCameraCharacteristics(id)
            CameraInfo(
                id = id,
                facing = characteristics?.get(
                    android.hardware.camera2.CameraCharacteristics.LENS_FACING
                ) ?: -1
            )
        }
    }

    data class CameraInfo(
        val id: String,
        val facing: Int
    ) {
        val isFrontCamera: Boolean get() = facing == android.hardware.camera2.CameraCharacteristics.LENS_FACING_FRONT
        val isBackCamera: Boolean get() = facing == android.hardware.camera2.CameraCharacteristics.LENS_FACING_BACK
    }
}
