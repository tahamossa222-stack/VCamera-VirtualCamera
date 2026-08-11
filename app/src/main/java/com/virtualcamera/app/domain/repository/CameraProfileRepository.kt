package com.virtualcamera.app.domain.repository

import com.virtualcamera.app.domain.model.CameraProfile
import kotlinx.coroutines.flow.Flow

interface CameraProfileRepository {
    fun getAllCameraProfiles(): Flow<List<CameraProfile>>
    suspend fun getCameraProfileById(id: Long): CameraProfile?
    suspend fun getActiveCameraProfile(): CameraProfile?
    suspend fun insertCameraProfile(cameraProfile: CameraProfile): Long
    suspend fun updateCameraProfile(cameraProfile: CameraProfile)
    suspend fun deleteCameraProfile(cameraProfile: CameraProfile)
    suspend fun deactivateAllProfiles()
    suspend fun activateProfile(id: Long)
}
