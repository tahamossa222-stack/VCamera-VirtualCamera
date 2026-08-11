package com.virtualcamera.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.virtualcamera.app.data.local.dao.CameraProfileDao
import com.virtualcamera.app.data.local.dao.MediaSourceDao
import com.virtualcamera.app.data.local.entity.CameraProfileEntity
import com.virtualcamera.app.data.local.entity.MediaSourceEntity

@Database(
    entities = [
        MediaSourceEntity::class,
        CameraProfileEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class VCameraDatabase : RoomDatabase() {
    abstract fun mediaSourceDao(): MediaSourceDao
    abstract fun cameraProfileDao(): CameraProfileDao
}
