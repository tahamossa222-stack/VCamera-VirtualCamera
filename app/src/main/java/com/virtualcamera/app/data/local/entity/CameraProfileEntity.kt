package com.virtualcamera.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "camera_profiles")
data class CameraProfileEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val cameraId: String,
    val isActive: Boolean = false,
    val facing: String,
    val sensorOrientation: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)
