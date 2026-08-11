package com.virtualcamera.app.data.repository

import com.virtualcamera.app.data.local.dao.MediaSourceDao
import com.virtualcamera.app.data.mapper.toDomain
import com.virtualcamera.app.data.mapper.toEntity
import com.virtualcamera.app.domain.model.MediaSource
import com.virtualcamera.app.domain.repository.MediaSourceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MediaSourceRepositoryImpl @Inject constructor(
    private val mediaSourceDao: MediaSourceDao
) : MediaSourceRepository {

    override fun getAllMediaSources(): Flow<List<MediaSource>> {
        return mediaSourceDao.getAllMediaSources().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getMediaSourcesByType(type: String): Flow<List<MediaSource>> {
        return mediaSourceDao.getMediaSourcesByType(type).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getMediaSourceById(id: Long): MediaSource? {
        return mediaSourceDao.getMediaSourceById(id)?.toDomain()
    }

    override suspend fun insertMediaSource(mediaSource: MediaSource): Long {
        return mediaSourceDao.insertMediaSource(mediaSource.toEntity())
    }

    override suspend fun updateMediaSource(mediaSource: MediaSource) {
        mediaSourceDao.updateMediaSource(mediaSource.toEntity())
    }

    override suspend fun deleteMediaSource(mediaSource: MediaSource) {
        mediaSourceDao.deleteMediaSource(mediaSource.toEntity())
    }

    override suspend fun deleteMediaSourceById(id: Long) {
        mediaSourceDao.deleteMediaSourceById(id)
    }

    override suspend fun updateLastUsedAt(id: Long) {
        mediaSourceDao.updateLastUsedAt(id)
    }

    override suspend fun getMediaSourceCount(): Int {
        return mediaSourceDao.getMediaSourceCount()
    }
}
