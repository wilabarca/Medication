package com.example.medication.core.di

import com.example.medication.features.auth.data.dataresources.remote.api.AuthApi
import com.example.medication.features.searchmedication.data.datasources.remote.api.MedicationApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetWorkModule {

    @Provides
    @Singleton
    @ApiMedicationRetrofit
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://44.222.56.219:3000/")  // ✅ EC2
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(
        @ApiMedicationRetrofit retrofit: Retrofit
    ): AuthApi = retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideMedicineApiService(
        @ApiMedicationRetrofit retrofit: Retrofit
    ): MedicationApiService = retrofit.create(MedicationApiService::class.java)
}