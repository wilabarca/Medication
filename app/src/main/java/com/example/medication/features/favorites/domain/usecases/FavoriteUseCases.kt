package com.example.medication.features.favorites.domain.usecases

import javax.inject.Inject

// Wrapper que agrupa todos los use cases — patrón del profe
data class FavoriteUseCases @Inject constructor(
    val getFavorites: GetFavoritesUseCase,
    val toggleFavorite: ToggleFavoriteUseCase,
    val isFavorite: IsFavoriteUseCase
)