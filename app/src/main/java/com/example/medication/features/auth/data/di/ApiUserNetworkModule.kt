package com.example.medication.features.auth.data.di

import com.example.medication.core.di.ApiMedicationRetrofit
import com.example.medication.features.auth.data.dataresources.remote.api.AuthApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ApiUserNetworkModule {

    @Provides
    @Singleton
    fun provideAuthApi(
        @ApiMedicationRetrofit retrofit: Retrofit
    ): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }
}