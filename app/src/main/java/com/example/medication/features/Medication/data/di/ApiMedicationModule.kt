package com.example.Medication.features.Medication.data.di

import com.example.medication.core.di.ApiMedicationRetrofit
import com.example.medication.features.Medication.data.datasources.remote.api.MedicationApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ApiMedicationModule {

    @Provides
    @Singleton
    fun provideMedicationApi(
        @ApiMedicationRetrofit retrofit: Retrofit
    ): MedicationApi{
        return retrofit.create(MedicationApi::class.java)
    }
}