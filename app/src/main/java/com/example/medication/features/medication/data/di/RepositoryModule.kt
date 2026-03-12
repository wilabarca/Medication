package com.example.medication.features.medication.data.di

import com.example.medication.features.medication.data.repositories.MedicationRepositoryImpl
import com.example.medication.features.medication.domain.repositories.MedicationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindMedicationRepository(
        impl: MedicationRepositoryImpl
    ): MedicationRepository
}