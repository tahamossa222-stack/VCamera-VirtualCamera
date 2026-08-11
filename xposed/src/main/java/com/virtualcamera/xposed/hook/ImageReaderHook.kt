package com.virtualcamera.xposed.hook

import android.media.ImageReader
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers

object ImageReaderHook {

    private const val TAG = "VCamera-ImageReader"

    fun hook(classLoader: ClassLoader) {
        try {
            XposedHelpers.findAndHookMethod(
                ImageReader::class.java,
                "newInstance",
                Int::class.java,
                Int::class.java,
                Int::class.java,
                Int::class.java,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        val width = param.args[0] as Int
                        val height = param.args[1] as Int
                        val format = param.args[2] as Int
                        val maxImages = param.args[3] as Int
                        XposedBridge.log("$TAG: newInstance intercepted: ${width}x${height}, format=$format, maxImages=$maxImages")
                    }
                }
            )

            XposedHelpers.findAndHookMethod(
                ImageReader::class.java,
                "acquireLatestImage",
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        val image = param.result
                        XposedBridge.log("$TAG: acquireLatestImage intercepted")
                    }
                }
            )

            XposedHelpers.findAndHookMethod(
                ImageReader::class.java,
                "acquireNextImage",
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        XposedBridge.log("$TAG: acquireNextImage intercepted")
                    }
                }
            )

            XposedBridge.log("$TAG: Hooks installed successfully")
        } catch (e: Throwable) {
            XposedBridge.log("$TAG: Failed to install hooks: ${e.message}")
        }
    }
}
