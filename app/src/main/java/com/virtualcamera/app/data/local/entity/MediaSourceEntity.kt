package com.virtualcamera.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "media_sources")
data class MediaSourceEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val path: String,
    val type: String,
    val protocol: String? = null,
    val thumbnailPath: String? = null,
    val duration: Long? = null,
    val width: Int? = null,
    val height: Int? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val lastUsedAt: Long = System.currentTimeMillis()
)
