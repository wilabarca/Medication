package com.example.medication.features.history.data.di

import com.example.medication.features.history.data.repositories.MedicationHistoryRepositoryImpl
import com.example.medication.features.history.domain.repositories.MedicationHistoryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HistoryModule {

    @Binds
    @Singleton
    abstract fun bindHistoryRepository(
        impl: MedicationHistoryRepositoryImpl
    ): MedicationHistoryRepository
}