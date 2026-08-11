package com.virtualcamera.xposed.bridge

import android.content.Context
import android.content.SharedPreferences
import android.view.Surface
import com.virtualcamera.xposed.interceptor.CameraInterceptor
import com.virtualcamera.xposed.interceptor.SurfaceInterceptor

object HookBridge {

    private const val PREFS_NAME = "vcamera_bridge"
    private const val KEY_ENABLED = "virtual_camera_enabled"
    private const val KEY_SOURCE_URL = "source_url"

    private var cameraInterceptor: CameraInterceptor? = null
    private var surfaceInterceptor: SurfaceInterceptor? = null
    private var sharedPreferences: SharedPreferences? = null

    fun initialize(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        cameraInterceptor = CameraInterceptor()
        surfaceInterceptor = SurfaceInterceptor()
    }

    fun isEnabled(): Boolean {
        return sharedPreferences?.getBoolean(KEY_ENABLED, false) ?: false
    }

    fun getSourceUrl(): String? {
        return sharedPreferences?.getString(KEY_SOURCE_URL, null)
    }

    fun setEnabled(enabled: Boolean) {
        sharedPreferences?.edit()?.putBoolean(KEY_ENABLED, enabled)?.apply()
        if (enabled) {
            cameraInterceptor?.startIntercepting()
            surfaceInterceptor?.activate()
        } else {
            cameraInterceptor?.stopIntercepting()
            surfaceInterceptor?.deactivate()
        }
    }

    fun setSourceUrl(url: String) {
        sharedPreferences?.edit()?.putString(KEY_SOURCE_URL, url)?.apply()
    }

    fun getCameraInterceptor(): CameraInterceptor? = cameraInterceptor
    fun getSurfaceInterceptor(): SurfaceInterceptor? = surfaceInterceptor

    fun registerFakeSurface(original: Surface, fake: Surface) {
        cameraInterceptor?.registerFakeSurface(original, fake)
        surfaceInterceptor?.mapSurface(original, fake)
    }

    fun getFakeSurface(original: Surface): Surface? {
        return cameraInterceptor?.getFakeSurface(original)
            ?: surfaceInterceptor?.replaceSurface(original)
    }

    fun interceptSurfaces(surfaces: List<Surface>, fakeSurface: Surface): List<Surface> {
        return cameraInterceptor?.interceptCaptureSession(surfaces, fakeSurface) ?: surfaces
    }
}
