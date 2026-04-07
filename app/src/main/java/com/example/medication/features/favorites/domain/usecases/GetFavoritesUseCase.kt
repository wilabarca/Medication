package com.example.medication.features.favorites.domain.usecases

import com.example.medication.features.favorites.domain.repositories.FavoriteRepository
import com.example.medication.features.medication.domain.entities.Medication
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoritesUseCase @Inject constructor(
    private val repository: FavoriteRepository
) {
    operator fun invoke(): Flow<List<Medication>> {
        return repository.getAllFavorites()
    }
}