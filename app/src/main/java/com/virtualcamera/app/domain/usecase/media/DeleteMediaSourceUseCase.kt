package com.virtualcamera.app.domain.usecase.media

import com.virtualcamera.app.domain.model.MediaSource
import com.virtualcamera.app.domain.repository.MediaSourceRepository
import com.virtualcamera.core.common.Result
import javax.inject.Inject

class DeleteMediaSourceUseCase @Inject constructor(
    private val repository: MediaSourceRepository
) {
    suspend operator fun invoke(mediaSource: MediaSource): Result<Unit> {
        return Result.runCatching {
            repository.deleteMediaSource(mediaSource)
        }
    }

    suspend fun byId(id: Long): Result<Unit> {
        return Result.runCatching {
            repository.deleteMediaSourceById(id)
        }
    }
}
