package com.virtualcamera.app.presentation.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object MediaPicker : Screen("media_picker")
    data object MediaPreview : Screen("media_preview/{mediaId}") {
        fun createRoute(mediaId: Long) = "media_preview/$mediaId"
    }
    data object StreamInput : Screen("stream_input")
    data object CameraSettings : Screen("camera_settings")
    data object CameraPreview : Screen("camera_preview")
    data object Settings : Screen("settings")
}
