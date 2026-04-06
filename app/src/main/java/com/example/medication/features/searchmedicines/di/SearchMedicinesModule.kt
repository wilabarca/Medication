package com.example.medication.features.searchmedicines.data.di

import com.example.medication.features.searchmedicines.data.repository.MedicineRepositoryImpl
import com.example.medication.features.searchmedicines.domain.repositories.MedicineRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SearchMedicinesModule {

    @Binds
    @Singleton
    abstract fun bindMedicineRepository(
        impl: MedicineRepositoryImpl
    ): MedicineRepository
}