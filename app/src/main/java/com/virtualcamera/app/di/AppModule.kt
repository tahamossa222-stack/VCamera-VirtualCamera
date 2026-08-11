package com.virtualcamera.app.di

import android.content.Context
import androidx.room.Room
import com.virtualcamera.app.data.local.VCameraDatabase
import com.virtualcamera.app.data.local.dao.CameraProfileDao
import com.virtualcamera.app.data.local.dao.MediaSourceDao
import com.virtualcamera.core.common.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): VCameraDatabase {
        return Room.databaseBuilder(
            context,
            VCameraDatabase::class.java,
            Constants.DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideMediaSourceDao(database: VCameraDatabase): MediaSourceDao {
        return database.mediaSourceDao()
    }

    @Provides
    fun provideCameraProfileDao(database: VCameraDatabase): CameraProfileDao {
        return database.cameraProfileDao()
    }
}
