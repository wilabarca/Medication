package com.example.medication.features.favorites.data.repositories

import com.example.medication.core.database.dao.FavoriteDao
import com.example.medication.core.database.mapper.toFavoriteEntity
import com.example.medication.core.database.mapper.toMedicationList
import com.example.medication.features.favorites.domain.repositories.FavoriteRepository
import com.example.medication.features.medication.domain.entities.Medication
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val dao: FavoriteDao
) : FavoriteRepository {

    override suspend fun addFavorite(medication: Medication) {
        dao.addFavorite(medication.toFavoriteEntity())
    }

    override suspend fun removeFavorite(id: String) {
        dao.removeFavorite(id)
    }

    override suspend fun getAllFavorites(): List<Medication> {
        return dao.getAllFavorites().toMedicationList()
    }

    override suspend fun isFavorite(id: String): Boolean {
        return dao.isFavorite(id)
    }
}