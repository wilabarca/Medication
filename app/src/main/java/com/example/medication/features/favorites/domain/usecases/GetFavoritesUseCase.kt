// GetFavoritesUseCase.kt
package com.example.medication.features.favorites.domain.usecases

import com.example.medication.features.favorites.domain.repositories.FavoriteRepository
import com.example.medication.features.medication.domain.entities.Medication
import javax.inject.Inject

class GetFavoritesUseCase @Inject constructor(
    private val repository: FavoriteRepository
) {
    suspend operator fun invoke(): List<Medication> {
        return repository.getAllFavorites()
    }
}