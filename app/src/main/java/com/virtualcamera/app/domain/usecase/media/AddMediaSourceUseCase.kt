package com.virtualcamera.app.domain.usecase.media

import com.virtualcamera.app.domain.model.MediaSource
import com.virtualcamera.app.domain.repository.MediaSourceRepository
import com.virtualcamera.core.common.Constants
import com.virtualcamera.core.common.Result
import javax.inject.Inject

class AddMediaSourceUseCase @Inject constructor(
    private val repository: MediaSourceRepository
) {
    suspend operator fun invoke(mediaSource: MediaSource): Result<Long> {
        return Result.runCatching {
            val count = repository.getMediaSourceCount()
            if (count >= Constants.MAX_MEDIA_SOURCES) {
                throw IllegalStateException("Maximum media sources limit reached")
            }
            repository.insertMediaSource(mediaSource)
        }
    }
}
