package com.virtualcamera.app.di

import com.virtualcamera.app.data.repository.CameraProfileRepositoryImpl
import com.virtualcamera.app.data.repository.MediaSourceRepositoryImpl
import com.virtualcamera.app.domain.repository.CameraProfileRepository
import com.virtualcamera.app.domain.repository.MediaSourceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMediaSourceRepository(
        impl: MediaSourceRepositoryImpl
    ): MediaSourceRepository

    @Binds
    @Singleton
    abstract fun bindCameraProfileRepository(
        impl: CameraProfileRepositoryImpl
    ): CameraProfileRepository
}
