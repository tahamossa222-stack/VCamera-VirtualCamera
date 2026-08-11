package com.virtualcamera.app.domain.model

data class CameraProfile(
    val id: Long = 0,
    val name: String,
    val cameraId: String,
    val isActive: Boolean = false,
    val facing: String,
    val sensorOrientation: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
) {
    val isFrontCamera: Boolean get() = facing == "front"
    val isBackCamera: Boolean get() = facing == "back"
}
