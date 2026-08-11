package com.virtualcamera.xposed.hook

import android.hardware.camera2.CaptureRequest
import android.view.Surface
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers

object CaptureRequestHook {

    private const val TAG = "VCamera-CaptureRequest"
    private val trackedSurfaces = mutableSetOf<Surface>()

    fun hook(classLoader: ClassLoader) {
        try {
            val builderClass = XposedHelpers.findClass(
                "android.hardware.camera2.CaptureRequest\$Builder",
                classLoader
            )

            XposedHelpers.findAndHookMethod(
                builderClass,
                "addTarget",
                Surface::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        val surface = param.args[0] as? Surface ?: return
                        XposedBridge.log("$TAG: addTarget intercepted")
                    }
                }
            )

            XposedHelpers.findAndHookMethod(
                builderClass,
                "removeTarget",
                Surface::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        val surface = param.args[0] as? Surface ?: return
                        XposedBridge.log("$TAG: removeTarget intercepted")
                    }
                }
            )

            XposedBridge.log("$TAG: Hooks installed successfully")
        } catch (e: Throwable) {
            XposedBridge.log("$TAG: Failed to install hooks: ${e.message}")
        }
    }

    fun addTrackedSurface(surface: Surface) {
        trackedSurfaces.add(surface)
    }

    fun removeTrackedSurface(surface: Surface) {
        trackedSurfaces.remove(surface)
    }

    fun isTrackedSurface(surface: Surface): Boolean {
        return surface in trackedSurfaces
    }
}
