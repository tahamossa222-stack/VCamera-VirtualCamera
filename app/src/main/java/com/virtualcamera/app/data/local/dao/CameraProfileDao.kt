package com.virtualcamera.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.virtualcamera.app.data.local.entity.CameraProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CameraProfileDao {
    @Query("SELECT * FROM camera_profiles ORDER BY createdAt DESC")
    fun getAllCameraProfiles(): Flow<List<CameraProfileEntity>>

    @Query("SELECT * FROM camera_profiles WHERE id = :id")
    suspend fun getCameraProfileById(id: Long): CameraProfileEntity?

    @Query("SELECT * FROM camera_profiles WHERE isActive = 1 LIMIT 1")
    suspend fun getActiveCameraProfile(): CameraProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCameraProfile(cameraProfile: CameraProfileEntity): Long

    @Update
    suspend fun updateCameraProfile(cameraProfile: CameraProfileEntity)

    @Delete
    suspend fun deleteCameraProfile(cameraProfile: CameraProfileEntity)

    @Query("UPDATE camera_profiles SET isActive = 0")
    suspend fun deactivateAllProfiles()

    @Query("UPDATE camera_profiles SET isActive = 1 WHERE id = :id")
    suspend fun activateProfile(id: Long)
}
