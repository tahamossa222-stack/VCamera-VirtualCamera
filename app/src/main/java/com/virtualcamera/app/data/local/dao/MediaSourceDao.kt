package com.virtualcamera.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.virtualcamera.app.data.local.entity.MediaSourceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MediaSourceDao {
    @Query("SELECT * FROM media_sources ORDER BY lastUsedAt DESC")
    fun getAllMediaSources(): Flow<List<MediaSourceEntity>>

    @Query("SELECT * FROM media_sources WHERE id = :id")
    suspend fun getMediaSourceById(id: Long): MediaSourceEntity?

    @Query("SELECT * FROM media_sources WHERE type = :type ORDER BY lastUsedAt DESC")
    fun getMediaSourcesByType(type: String): Flow<List<MediaSourceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMediaSource(mediaSource: MediaSourceEntity): Long

    @Update
    suspend fun updateMediaSource(mediaSource: MediaSourceEntity)

    @Delete
    suspend fun deleteMediaSource(mediaSource: MediaSourceEntity)

    @Query("DELETE FROM media_sources WHERE id = :id")
    suspend fun deleteMediaSourceById(id: Long)

    @Query("UPDATE media_sources SET lastUsedAt = :timestamp WHERE id = :id")
    suspend fun updateLastUsedAt(id: Long, timestamp: Long = System.currentTimeMillis())

    @Query("SELECT COUNT(*) FROM media_sources")
    suspend fun getMediaSourceCount(): Int
}
