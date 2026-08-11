package com.virtualcamera.xposed.interceptor

import android.hardware.camera2.CameraDevice
import android.view.Surface
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers

class CameraInterceptor {

    private val interceptedSurfaces = mutableMapOf<Surface, Surface>()
    private var isIntercepting = false

    fun startIntercepting() {
        isIntercepting = true
        XposedBridge.log("CameraInterceptor: Started intercepting")
    }

    fun stopIntercepting() {
        isIntercepting = false
        interceptedSurfaces.clear()
        XposedBridge.log("CameraInterceptor: Stopped intercepting")
    }

    fun isIntercepting(): Boolean = isIntercepting

    fun registerFakeSurface(original: Surface, fake: Surface) {
        interceptedSurfaces[original] = fake
        XposedBridge.log("CameraInterceptor: Registered fake surface for original")
    }

    fun getFakeSurface(original: Surface): Surface? {
        return if (isIntercepting) interceptedSurfaces[original] else null
    }

    fun interceptCaptureSession(
        surfaces: List<Surface>,
        fakeSurface: Surface
    ): List<Surface> {
        if (!isIntercepting) return surfaces

        return surfaces.map { surface ->
            interceptedSurfaces[surface] ?: surface
        }
    }
}
