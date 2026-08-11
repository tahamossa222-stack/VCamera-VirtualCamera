package com.virtualcamera.xposed

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import com.virtualcamera.xposed.hook.CameraManagerHook
import com.virtualcamera.xposed.hook.CameraDeviceHook
import com.virtualcamera.xposed.hook.CaptureRequestHook
import com.virtualcamera.xposed.hook.ImageReaderHook
import com.virtualcamera.xposed.hook.Camera1Hook

class XposedModule : IXposedHookLoadPackage {

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName == "com.virtualcamera.app") return

        try {
            val classLoader = lpparam.classLoader

            CameraManagerHook.hook(classLoader)
            CameraDeviceHook.hook(classLoader)
            CaptureRequestHook.hook(classLoader)
            ImageReaderHook.hook(classLoader)
            Camera1Hook.hook(classLoader)

            XposedBridge.log("VCamera Xposed module loaded for: ${lpparam.packageName}")
        } catch (e: Throwable) {
            XposedBridge.log("VCamera: Failed to hook ${lpparam.packageName}: ${e.message}")
        }
    }
}
