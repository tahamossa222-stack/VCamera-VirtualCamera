package com.virtualcamera.app.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.virtualcamera.app.camera.CameraHookManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CameraHookService : Service() {

    @Inject
    lateinit var cameraHookManager: CameraHookManager

    private val binder = LocalBinder()

    inner class LocalBinder : Binder() {
        fun getService(): CameraHookService = this@CameraHookService
    }

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_INSTALL_HOOKS -> installHooks()
            ACTION_UNINSTALL_HOOKS -> uninstallHooks()
        }
        return START_STICKY
    }

    fun installHooks() {
        val notification = createNotification("Camera hooks installed")
        startForeground(NOTIFICATION_ID, notification)
        cameraHookManager.installHooks()
    }

    fun uninstallHooks() {
        cameraHookManager.uninstallHooks()
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    fun isHooksInstalled(): Boolean = cameraHookManager.isHooksInstalled()

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Camera Hook Service",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Manages camera hooks"
        }
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    private fun createNotification(text: String): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("VCamera Hooks")
            .setContentText(text)
            .setSmallIcon(android.R.drawable.ic_menu_camera)
            .setOngoing(true)
            .build()
    }

    companion object {
        const val CHANNEL_ID = "camera_hook_channel"
        const val NOTIFICATION_ID = 2
        const val ACTION_INSTALL_HOOKS = "com.virtualcamera.ACTION_INSTALL_HOOKS"
        const val ACTION_UNINSTALL_HOOKS = "com.virtualcamera.ACTION_UNINSTALL_HOOKS"
    }
}
