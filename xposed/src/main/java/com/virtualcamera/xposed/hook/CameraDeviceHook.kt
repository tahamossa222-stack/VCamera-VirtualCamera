package com.virtualcamera.xposed.hook

import android.hardware.camera2.CameraDevice
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers

object CameraDeviceHook {

    private const val TAG = "VCamera-CameraDevice"

    fun hook(classLoader: ClassLoader) {
        try {
            XposedHelpers.findAndHookMethod(
                CameraDevice::class.java,
                "createCaptureSession",
                java.util.List::class.java,
                CameraDevice.StateCallback::class.java,
                android.os.Handler::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        val surfaces = param.args[0] as? List<*> ?: return
                        XposedBridge.log("$TAG: createCaptureSession intercepted with ${surfaces.size} surfaces")
                    }
                }
            )

            XposedHelpers.findAndHookMethod(
                CameraDevice::class.java,
                "createCaptureSession",
                android.hardware.camera2.params.SessionConfiguration::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        XposedBridge.log("$TAG: createCaptureSession (SessionConfiguration) intercepted")
                    }
                }
            )

            XposedBridge.log("$TAG: Hooks installed successfully")
        } catch (e: Throwable) {
            XposedBridge.log("$TAG: Failed to install hooks: ${e.message}")
        }
    }
}
