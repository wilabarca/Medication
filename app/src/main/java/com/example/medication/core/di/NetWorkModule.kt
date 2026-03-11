package com.example.medication.core.di

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
            .baseUrl("https://localhost:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(
        @ApiMedicationRetrofit retrofit: Retrofit
    ):AuthApi{
        return retrofit.create(AuthApi::class.java)
    }

}