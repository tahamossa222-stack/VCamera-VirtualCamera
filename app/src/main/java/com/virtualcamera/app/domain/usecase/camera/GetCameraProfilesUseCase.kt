package com.virtualcamera.app.domain.usecase.camera

import com.virtualcamera.app.domain.model.CameraProfile
import com.virtualcamera.app.domain.repository.CameraProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCameraProfilesUseCase @Inject constructor(
    private val repository: CameraProfileRepository
) {
    operator fun invoke(): Flow<List<CameraProfile>> {
        return repository.getAllCameraProfiles()
    }

    suspend fun getActive(): CameraProfile? {
        return repository.getActiveCameraProfile()
    }
}
