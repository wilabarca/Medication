package com.example.medication.features.patients.data.di

import com.example.medication.features.patients.data.repositories.PatientRepositoryImpl
import com.example.medication.features.patients.domain.repositories.PatientRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PatientsRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindPatientRepository(
        impl: PatientRepositoryImpl
    ): PatientRepository
}