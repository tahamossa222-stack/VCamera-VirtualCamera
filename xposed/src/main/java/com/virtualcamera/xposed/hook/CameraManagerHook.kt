package com.virtualcamera.xposed.hook

import android.hardware.camera2.CameraManager
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers

object CameraManagerHook {

    private const val TAG = "VCamera-CameraManager"

    fun hook(classLoader: ClassLoader) {
        try {
            XposedHelpers.findAndHookMethod(
                CameraManager::class.java,
                "getCameraIdList",
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        val result = param.result as? Array<String> ?: return
                        XposedBridge.log("$TAG: getCameraIdList intercepted, found ${result.size} cameras")
                    }
                }
            )

            XposedHelpers.findAndHookMethod(
                CameraManager::class.java,
                "openCamera",
                String::class.java,
                android.hardware.camera2.CameraDevice.StateCallback::class.java,
                android.os.Handler::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        val cameraId = param.args[0] as? String ?: return
                        XposedBridge.log("$TAG: openCamera intercepted for camera: $cameraId")
                    }
                }
            )

            XposedHelpers.findAndHookMethod(
                CameraManager::class.java,
                "openCamera",
                String::class.java,
                java.util.concurrent.Executor::class.java,
                android.hardware.camera2.CameraDevice.StateCallback::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        val cameraId = param.args[0] as? String ?: return
                        XposedBridge.log("$TAG: openCamera (Executor) intercepted for camera: $cameraId")
                    }
                }
            )

            XposedHelpers.findAndHookMethod(
                CameraManager::class.java,
                "getCameraCharacteristics",
                String::class.java,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        val cameraId = param.args[0] as? String ?: return
                        XposedBridge.log("$TAG: getCameraCharacteristics intercepted for camera: $cameraId")
                    }
                }
            )

            XposedBridge.log("$TAG: Hooks installed successfully")
        } catch (e: Throwable) {
            XposedBridge.log("$TAG: Failed to install hooks: ${e.message}")
        }
    }
}
