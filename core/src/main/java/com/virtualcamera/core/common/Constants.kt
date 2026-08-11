package com.virtualcamera.core.common

object Constants {
    const val DATABASE_NAME = "vcamera_database"
    const val DEFAULT_CAMERA_ID = "0"
    const val MAX_MEDIA_SOURCES = 100
    const val DEFAULT_VIDEO_WIDTH = 1920
    const val DEFAULT_VIDEO_HEIGHT = 1080

    object Preferences {
        const val PREFS_NAME = "vcamera_prefs"
        const val KEY_VIRTUAL_CAMERA_ENABLED = "virtual_camera_enabled"
        const val KEY_CURRENT_SOURCE_ID = "current_source_id"
        const val KEY_AUDIO_ENABLED = "audio_enabled"
        const val KEY_LOOP_VIDEO = "loop_video"
    }

    object Streams {
        const val RTSP_DEFAULT_PORT = 554
        const val HLS_SEGMENT_TARGET_DURATION = 6
        const val MAX_RETRY_ATTEMPTS = 3
        const val RECONNECT_DELAY_MS = 3000L
    }

    object Camera {
        const val OPEN_TIMEOUT_MS = 5000L
        const val SESSION_TIMEOUT_MS = 5000L
        const val FRAME_RATE = 30
    }
}
