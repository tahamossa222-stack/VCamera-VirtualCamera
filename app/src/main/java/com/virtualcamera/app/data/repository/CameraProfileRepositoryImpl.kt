package com.virtualcamera.app.data.repository

import com.virtualcamera.app.data.local.dao.CameraProfileDao
import com.virtualcamera.app.data.mapper.toDomain
import com.virtualcamera.app.data.mapper.toEntity
import com.virtualcamera.app.domain.model.CameraProfile
import com.virtualcamera.app.domain.repository.CameraProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CameraProfileRepositoryImpl @Inject constructor(
    private val cameraProfileDao: CameraProfileDao
) : CameraProfileRepository {

    override fun getAllCameraProfiles(): Flow<List<CameraProfile>> {
        return cameraProfileDao.getAllCameraProfiles().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getCameraProfileById(id: Long): CameraProfile? {
        return cameraProfileDao.getCameraProfileById(id)?.toDomain()
    }

    override suspend fun getActiveCameraProfile(): CameraProfile? {
        return cameraProfileDao.getActiveCameraProfile()?.toDomain()
    }

    override suspend fun insertCameraProfile(cameraProfile: CameraProfile): Long {
        return cameraProfileDao.insertCameraProfile(cameraProfile.toEntity())
    }

    override suspend fun updateCameraProfile(cameraProfile: CameraProfile) {
        cameraProfileDao.updateCameraProfile(cameraProfile.toEntity())
    }

    override suspend fun deleteCameraProfile(cameraProfile: CameraProfile) {
        cameraProfileDao.deleteCameraProfile(cameraProfile.toEntity())
    }

    override suspend fun deactivateAllProfiles() {
        cameraProfileDao.deactivateAllProfiles()
    }

    override suspend fun activateProfile(id: Long) {
        cameraProfileDao.activateProfile(id)
    }
}
