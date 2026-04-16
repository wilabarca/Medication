package com.example.medication.core.di

import android.content.Context
import com.example.medication.core.hardware.data.AndroidAlarmManager
import com.example.medication.core.hardware.data.AndroidCameraManager
import com.example.medication.core.hardware.data.AndroidDeviceIdProvider
import com.example.medication.core.hardware.data.AndroidVibrateManager
import com.example.medication.core.hardware.domain.CameraManager
import com.example.medication.core.hardware.domain.DeviceIdProvider
import com.example.medication.core.hardware.domain.MedicationAlarmScheduler
import com.example.medication.core.hardware.domain.VibrateManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HardwareModule {

    @Provides
    @Singleton
    fun provideVibrateManager(
        @ApplicationContext context: Context
    ): VibrateManager = AndroidVibrateManager(context)

    @Provides
    @Singleton
    fun provideCameraManager(
        @ApplicationContext context: Context
    ): CameraManager = AndroidCameraManager(context)

    @Provides
    @Singleton
    fun provideMedicationAlarmScheduler(
        @ApplicationContext context: Context
    ): MedicationAlarmScheduler = AndroidAlarmManager(context)

    @Provides
    @Singleton
    fun provideDeviceIdProvider(
        @ApplicationContext context: Context
    ): DeviceIdProvider = AndroidDeviceIdProvider(context)
}