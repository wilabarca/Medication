package com.example.medication.features.medication.data.datasources.local.alarm.di

import android.content.Context
import com.example.medication.core.hardware.data.AndroidAlarmManager
import com.example.medication.core.hardware.domain.MedicationAlarmScheduler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AlarmModule {

    @Provides
    @Singleton
    fun provideAlarmScheduler(
        @ApplicationContext context: Context
    ): MedicationAlarmScheduler = AndroidAlarmManager(context)  // ← usar la clase existente
}