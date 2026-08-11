package com.virtualcamera.app.domain.usecase.media

import com.virtualcamera.app.domain.model.MediaSource
import com.virtualcamera.app.domain.repository.MediaSourceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMediaSourcesUseCase @Inject constructor(
    private val repository: MediaSourceRepository
) {
    operator fun invoke(): Flow<List<MediaSource>> {
        return repository.getAllMediaSources()
    }

    fun getByType(type: String): Flow<List<MediaSource>> {
        return repository.getMediaSourcesByType(type)
    }
}
