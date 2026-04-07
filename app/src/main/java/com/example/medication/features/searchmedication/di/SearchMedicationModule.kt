package com.example.medication.features.searchmedication.data.di

import com.example.medication.features.searchmedication.data.datasources.remote.api.MedicationApiService
import com.example.medication.features.searchmedication.data.repositories.MedicationRepositoryImpl
import com.example.medication.features.searchmedication.domain.repositories.MedicationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SearchMedicationModule {

    @Binds
    @Singleton
    abstract fun bindMedicationRepository(
        impl: MedicationRepositoryImpl
    ): MedicationRepository
}