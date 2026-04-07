package com.example.medication.features.favorites.data.repositories

import com.example.medication.core.database.dao.FavoriteDao
import com.example.medication.features.favorites.data.datasources.local.mapper.toDomain
import com.example.medication.features.favorites.data.datasources.local.mapper.toEntity
import com.example.medication.features.favorites.domain.repositories.FavoriteRepository
import com.example.medication.features.medication.domain.entities.Medication
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val dao: FavoriteDao
) : FavoriteRepository {

    // Flow reactivo — igual que el profe con getPosts()
    override fun getAllFavorites(): Flow<List<Medication>> {
        return dao.getAllFavorites().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun addFavorite(medication: Medication) {
        dao.addFavorite(medication.toEntity())
    }

    override suspend fun removeFavorite(id: String) {
        dao.removeFavorite(id)
    }

    override suspend fun isFavorite(id: String): Boolean {
        return dao.isFavorite(id)
    }
}