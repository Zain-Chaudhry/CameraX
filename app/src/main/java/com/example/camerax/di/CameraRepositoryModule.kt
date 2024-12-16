package com.example.camerax.di

import android.app.Application
import com.example.camerax.MainActivity
import com.example.camerax.data.repository.CameraRepositoryImpl
import com.example.camerax.domain.repository.CameraRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CameraRepositoryModule {

    @Singleton
    @Binds
    abstract fun bindCameraRepository(
        cameraRepositoryImpl: CameraRepositoryImpl
    ): CameraRepository

}