package com.virtualcamera.xposed.hook

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers

object Camera1Hook {

    private const val TAG = "VCamera-Camera1"

    fun hook(classLoader: ClassLoader) {
        try {
            val cameraClass = XposedHelpers.findClass(
                "android.hardware.Camera",
                classLoader
            )

            XposedHelpers.findAndHookMethod(
                cameraClass,
                "open",
                Int::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        val cameraId = param.args[0] as? Int ?: return
                        XposedBridge.log("$TAG: Camera.open intercepted for camera: $cameraId")
                    }
                }
            )

            XposedHelpers.findAndHookMethod(
                cameraClass,
                "open",
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        XposedBridge.log("$TAG: Camera.open() intercepted")
                    }
                }
            )

            XposedBridge.log("$TAG: Hooks installed successfully")
        } catch (e: Throwable) {
            XposedBridge.log("$TAG: Failed to install hooks: ${e.message}")
        }
    }
}
