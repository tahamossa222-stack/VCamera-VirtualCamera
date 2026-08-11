package com.virtualcamera.app.domain.usecase.camera

import com.virtualcamera.app.domain.repository.CameraProfileRepository
import com.virtualcamera.core.common.Result
import javax.inject.Inject

class SwitchCameraProfileUseCase @Inject constructor(
    private val repository: CameraProfileRepository
) {
    suspend operator fun invoke(profileId: Long): Result<Unit> {
        return Result.runCatching {
            repository.deactivateAllProfiles()
            repository.activateProfile(profileId)
        }
    }
}
