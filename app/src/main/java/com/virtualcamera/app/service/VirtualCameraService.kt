package com.virtualcamera.app.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.virtualcamera.app.R
import com.virtualcamera.app.camera.VirtualCameraEngine
import com.virtualcamera.app.domain.model.MediaSource
import com.virtualcamera.app.media.MediaPlaybackManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class VirtualCameraService : Service() {

    @Inject
    lateinit var virtualCameraEngine: VirtualCameraEngine

    @Inject
    lateinit var mediaPlaybackManager: MediaPlaybackManager

    private val binder = LocalBinder()

    inner class LocalBinder : Binder() {
        fun getService(): VirtualCameraService = this@VirtualCameraService
    }

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> {
                val sourceName = intent.getStringExtra(EXTRA_MEDIA_SOURCE_NAME)
                val sourcePath = intent.getStringExtra(EXTRA_MEDIA_SOURCE_PATH)
                if (sourceName != null && sourcePath != null) {
                    val source = MediaSource(
                        name = sourceName,
                        path = sourcePath,
                        type = com.virtualcamera.app.domain.model.MediaType.VIDEO
                    )
                    startVirtualCamera(source)
                }
            }
            ACTION_STOP -> {
                stopVirtualCamera()
            }
        }
        return START_STICKY
    }

    fun startVirtualCamera(source: MediaSource) {
        val notification = createNotification("Virtual Camera Active")
        startForeground(NOTIFICATION_ID, notification)

        mediaPlaybackManager.play(source, android.view.Surface(android.graphics.SurfaceTexture(0)))
    }

    fun stopVirtualCamera() {
        mediaPlaybackManager.stop()
        virtualCameraEngine.stop()
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    fun isRunning(): Boolean = mediaPlaybackManager.isCurrentlyPlaying()

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Virtual Camera Service",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Running virtual camera service"
        }
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    private fun createNotification(text: String): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("VCamera")
            .setContentText(text)
            .setSmallIcon(android.R.drawable.ic_menu_camera)
            .setOngoing(true)
            .build()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopVirtualCamera()
    }

    companion object {
        const val CHANNEL_ID = "virtual_camera_channel"
        const val NOTIFICATION_ID = 1
        const val ACTION_START = "com.virtualcamera.ACTION_START"
        const val ACTION_STOP = "com.virtualcamera.ACTION_STOP"
        const val EXTRA_MEDIA_SOURCE_NAME = "extra_media_source_name"
        const val EXTRA_MEDIA_SOURCE_PATH = "extra_media_source_path"
    }
}
