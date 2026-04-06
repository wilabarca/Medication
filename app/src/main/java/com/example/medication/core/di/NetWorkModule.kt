
package com.example.medication.core.di

import com.example.medication.features.auth.data.dataresources.remote.api.AuthApi
import com.example.medication.features.searchmedicines.data.remote.api.MedicineApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetWorkModule {

    @Provides
    @Singleton
    @ApiMedicationRetrofit
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://18.235.240.52:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(
        @ApiMedicationRetrofit retrofit: Retrofit
    ): AuthApi = retrofit.create(AuthApi::class.java)

    // ← agregar esto
    @Provides
    @Singleton
    fun provideMedicineApiService(
        @ApiMedicationRetrofit retrofit: Retrofit
    ): MedicineApiService = retrofit.create(MedicineApiService::class.java)
}