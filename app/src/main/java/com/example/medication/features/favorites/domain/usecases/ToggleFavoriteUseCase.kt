// ToggleFavoriteUseCase.kt
package com.example.medication.features.favorites.domain.usecases

import com.example.medication.features.favorites.domain.repositories.FavoriteRepository
import com.example.medication.features.medication.domain.entities.Medication
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val repository: FavoriteRepository
) {
    suspend operator fun invoke(medication: Medication) {
        if (repository.isFavorite(medication.id)) {
            repository.removeFavorite(medication.id)
        } else {
            repository.addFavorite(medication)
        }
    }
}