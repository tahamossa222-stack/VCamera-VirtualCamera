package com.virtualcamera.xposed.interceptor

import android.view.Surface
import de.robv.android.xposed.XposedBridge

class SurfaceInterceptor {

    private val surfaceMap = mutableMapOf<Surface, Surface>()
    private var isActive = false

    fun activate() {
        isActive = true
        XposedBridge.log("SurfaceInterceptor: Activated")
    }

    fun deactivate() {
        isActive = false
        surfaceMap.clear()
        XposedBridge.log("SurfaceInterceptor: Deactivated")
    }

    fun isActive(): Boolean = isActive

    fun mapSurface(original: Surface, replacement: Surface) {
        surfaceMap[original] = replacement
        XposedBridge.log("SurfaceInterceptor: Mapped surface")
    }

    fun replaceSurface(original: Surface): Surface {
        return if (isActive) {
            surfaceMap[original] ?: original
        } else {
            original
        }
    }

    fun removeMapping(original: Surface) {
        surfaceMap.remove(original)
    }

    fun clearMappings() {
        surfaceMap.clear()
    }
}
