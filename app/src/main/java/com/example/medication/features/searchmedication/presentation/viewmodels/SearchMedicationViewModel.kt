package com.example.medication.features.searchmedication.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medication.features.searchmedication.domain.entities.Medication
import com.example.medication.features.searchmedication.domain.repositories.MedicationRepository
import com.example.medication.features.searchmedication.domain.usecases.SearchMedicationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SearchMedicinesUiState(
    val query: String = "",
    val results: List<Medication> = emptyList(),
    val isLoading: Boolean = false,
    val selectedMedication: Medication? = null,
    val errorMessage: String? = null
)

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchMedicinesViewModel @Inject constructor(
    private val searchMedicationUseCase: SearchMedicationUseCase,
    private val repository: MedicationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchMedicinesUiState())
    val uiState: StateFlow<SearchMedicinesUiState> = _uiState.asStateFlow()

    private val queryFlow = MutableStateFlow("")

    init {
        Log.d("SEARCH_DEBUG", "🔧 ViewModel init arrancó")

        viewModelScope.launch {
            try {
                Log.d("SEARCH_DEBUG", "⏳ Llamando ensureCacheLoaded...")
                _uiState.update { it.copy(isLoading = true) }
                repository.ensureCacheLoaded()
                Log.d("SEARCH_DEBUG", "✅ ensureCacheLoaded terminó")
                _uiState.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                Log.e("SEARCH_DEBUG", "❌ Error en ensureCacheLoaded: ${e.message}", e)
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }

        viewModelScope.launch {
            Log.d("SEARCH_DEBUG", "🔧 Corrutina de búsqueda arrancó")
            queryFlow
                .debounce(300L)
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    Log.d("SEARCH_DEBUG", "🔍 flatMapLatest query: '$query'")
                    _uiState.update { it.copy(isLoading = query.length >= 2) }
                    searchMedicationUseCase(query)
                        .catch { e ->
                            Log.e("SEARCH_DEBUG", "❌ Error en búsqueda: ${e.message}", e)
                            _uiState.update { it.copy(errorMessage = e.message, isLoading = false) }
                            emit(emptyList())
                        }
                }
                .collect { medicines ->
                    Log.d("SEARCH_DEBUG", "📋 Resultados recibidos: ${medicines.size}")
                    medicines.forEach { Log.d("SEARCH_DEBUG", "  → ${it.name}") }
                    _uiState.update {
                        it.copy(results = medicines, isLoading = false, errorMessage = null)
                    }
                }
        }
    }

    fun onQueryChanged(query: String) {
        Log.d("SEARCH_DEBUG", "✏️ onQueryChanged: '$query'")
        _uiState.update { it.copy(query = query) }
        queryFlow.value = query
    }

    fun onMedicineSelected(medication: Medication) {
        _uiState.update { it.copy(selectedMedication = medication) }
    }

    fun onDismissDetail() {
        _uiState.update { it.copy(selectedMedication = null) }
    }

    fun onClearSearch() {
        Log.d("SEARCH_DEBUG", "🗑️ onClearSearch")
        _uiState.update { SearchMedicinesUiState() }
        queryFlow.value = ""
    }
}