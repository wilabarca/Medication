package com.example.medication.features.favorites.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medication.features.favorites.domain.usecases.FavoriteUseCases
import com.example.medication.features.medication.domain.entities.Medication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FavoritesUiState(
    val favorites: List<Medication> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val favoriteUseCases: FavoriteUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    private val _favoritesMap = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val favoritesMap: StateFlow<Map<String, Boolean>> = _favoritesMap.asStateFlow()

    init {
        getFavorites()
    }

    private fun getFavorites() {
        favoriteUseCases.getFavorites().onEach { favorites ->
            _uiState.value = _uiState.value.copy(
                favorites = favorites,
                isLoading = false
            )
            // Actualiza el mapa automáticamente cuando cambian los favoritos
            val map = favorites.associate { it.id to true }
            _favoritesMap.value = map
        }.launchIn(viewModelScope)
    }

    fun toggleFavorite(medication: Medication) {
        viewModelScope.launch {
            favoriteUseCases.toggleFavorite(medication)
            // Actualiza el mapa local inmediatamente
            val current = _favoritesMap.value.toMutableMap()
            current[medication.id] = !(current[medication.id] ?: false)
            _favoritesMap.value = current
        }
    }

    fun checkIsFavorite(id: String) {
        viewModelScope.launch {
            val result = favoriteUseCases.isFavorite(id)
            val current = _favoritesMap.value.toMutableMap()
            current[id] = result
            _favoritesMap.value = current
        }
    }
}