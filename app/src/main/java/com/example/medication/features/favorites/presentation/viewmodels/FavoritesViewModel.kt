package com.example.medication.features.favorites.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medication.features.favorites.domain.usecases.GetFavoritesUseCase
import com.example.medication.features.favorites.domain.usecases.IsFavoriteUseCase
import com.example.medication.features.favorites.domain.usecases.ToggleFavoriteUseCase
import com.example.medication.features.medication.domain.entities.Medication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FavoritesUiState(
    val favorites: List<Medication> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val isFavoriteUseCase: IsFavoriteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    // mapa id -> isFavorite para que la MedicationCard sepa si pintar ⭐ llena o vacía
    private val _favoritesMap = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val favoritesMap: StateFlow<Map<String, Boolean>> = _favoritesMap.asStateFlow()

    fun getFavorites() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val favorites = getFavoritesUseCase()
                _uiState.value = _uiState.value.copy(
                    favorites = favorites,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error al cargar favoritos"
                )
            }
        }
    }

    fun toggleFavorite(medication: Medication) {
        viewModelScope.launch {
            toggleFavoriteUseCase(medication)
            // actualiza el mapa local inmediatamente
            val current = _favoritesMap.value.toMutableMap()
            current[medication.id] = !(current[medication.id] ?: false)
            _favoritesMap.value = current
            getFavorites()
        }
    }

    fun checkIsFavorite(id: String) {
        viewModelScope.launch {
            val result = isFavoriteUseCase(id)
            val current = _favoritesMap.value.toMutableMap()
            current[id] = result
            _favoritesMap.value = current
        }
    }
}