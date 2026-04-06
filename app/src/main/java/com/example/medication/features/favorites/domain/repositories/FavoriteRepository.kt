package com.example.medication.features.favorites.domain.repositories

import com.example.medication.features.medication.domain.entities.Medication

interface FavoriteRepository {
    suspend fun addFavorite(medication: Medication)
    suspend fun removeFavorite(id: String)
    suspend fun getAllFavorites(): List<Medication>
    suspend fun isFavorite(id: String): Boolean
}