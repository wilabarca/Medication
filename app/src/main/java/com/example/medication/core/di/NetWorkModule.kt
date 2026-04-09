package com.example.medication.core.di

import com.example.medication.features.auth.data.dataresources.remote.api.AuthApi
import com.example.medication.features.searchmedication.data.datasources.remote.api.MedicationApiService // ← nuevo paquete
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
            .baseUrl("http://192.168.0.11:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(
        @ApiMedicationRetrofit retrofit: Retrofit
<<<<<<< Luis_conexion
    ): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

=======
    ): AuthApi = retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideMedicineApiService(
        @ApiMedicationRetrofit retrofit: Retrofit
    ): MedicationApiService = retrofit.create(MedicationApiService::class.java)
>>>>>>> main
}