package com.example.medication.features.favorites.domain.repositories

import com.example.medication.features.medication.domain.entities.Medication
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    fun getAllFavorites(): Flow<List<Medication>>  // ← Flow reactivo
    suspend fun addFavorite(medication: Medication)
    suspend fun removeFavorite(id: String)
    suspend fun isFavorite(id: String): Boolean
}