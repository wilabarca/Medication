package com.example.medication.features.favorites.domain.usecases

import com.example.medication.features.favorites.domain.repositories.FavoriteRepository
import javax.inject.Inject

class IsFavoriteUseCase @Inject constructor(
    private val repository: FavoriteRepository
) {
    suspend operator fun invoke(id: String): Boolean {
        return repository.isFavorite(id)
    }
}