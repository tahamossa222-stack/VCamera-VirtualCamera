package com.virtualcamera.app.domain.repository

import com.virtualcamera.app.domain.model.MediaSource
import kotlinx.coroutines.flow.Flow

interface MediaSourceRepository {
    fun getAllMediaSources(): Flow<List<MediaSource>>
    fun getMediaSourcesByType(type: String): Flow<List<MediaSource>>
    suspend fun getMediaSourceById(id: Long): MediaSource?
    suspend fun insertMediaSource(mediaSource: MediaSource): Long
    suspend fun updateMediaSource(mediaSource: MediaSource)
    suspend fun deleteMediaSource(mediaSource: MediaSource)
    suspend fun deleteMediaSourceById(id: Long)
    suspend fun updateLastUsedAt(id: Long)
    suspend fun getMediaSourceCount(): Int
}
