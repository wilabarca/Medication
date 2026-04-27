package com.example.medication.features.caregiver.di

import com.example.medication.features.caregiver.data.repositories.CaregiverRepositoryImpl
import com.example.medication.features.caregiver.domain.repositories.CaregiverRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CaregiverModule {

    @Binds
    @Singleton
    abstract fun bindCaregiverRepository(
        impl: CaregiverRepositoryImpl
    ): CaregiverRepository
}