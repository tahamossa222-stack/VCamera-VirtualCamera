package com.virtualcamera.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class VCameraApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
