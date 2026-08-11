package com.virtualcamera.app.data.mapper

import com.virtualcamera.app.data.local.entity.CameraProfileEntity
import com.virtualcamera.app.data.local.entity.MediaSourceEntity
import com.virtualcamera.app.domain.model.CameraProfile
import com.virtualcamera.app.domain.model.MediaSource
import com.virtualcamera.app.domain.model.MediaType
import com.virtualcamera.app.domain.model.StreamProtocol

fun MediaSourceEntity.toDomain(): MediaSource {
    return MediaSource(
        id = id,
        name = name,
        path = path,
        type = MediaType.valueOf(type),
        protocol = protocol?.let { StreamProtocol.valueOf(it) },
        thumbnailPath = thumbnailPath,
        duration = duration,
        width = width,
        height = height,
        createdAt = createdAt,
        lastUsedAt = lastUsedAt
    )
}

fun MediaSource.toEntity(): MediaSourceEntity {
    return MediaSourceEntity(
        id = id,
        name = name,
        path = path,
        type = type.name,
        protocol = protocol?.name,
        thumbnailPath = thumbnailPath,
        duration = duration,
        width = width,
        height = height,
        createdAt = createdAt,
        lastUsedAt = lastUsedAt
    )
}

fun CameraProfileEntity.toDomain(): CameraProfile {
    return CameraProfile(
        id = id,
        name = name,
        cameraId = cameraId,
        isActive = isActive,
        facing = facing,
        sensorOrientation = sensorOrientation,
        createdAt = createdAt
    )
}

fun CameraProfile.toEntity(): CameraProfileEntity {
    return CameraProfileEntity(
        id = id,
        name = name,
        cameraId = cameraId,
        isActive = isActive,
        facing = facing,
        sensorOrientation = sensorOrientation,
        createdAt = createdAt
    )
}
