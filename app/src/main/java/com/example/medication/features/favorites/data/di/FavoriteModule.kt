package com.example.medication.features.favorites.data.di

import com.example.medication.features.favorites.data.repositories.FavoriteRepositoryImpl
import com.example.medication.features.favorites.domain.repositories.FavoriteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class FavoriteModule {

    @Binds
    @Singleton
    abstract fun bindFavoriteRepository(
        impl: FavoriteRepositoryImpl
    ): FavoriteRepository
}