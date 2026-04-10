package com.example.medication.features.searchmedication.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medication.core.hardware.domain.VibrateManager
import com.example.medication.features.searchmedication.domain.entities.Medication
import com.example.medication.features.searchmedication.domain.usecases.SearchMedicationUseCases
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
    private val searchMedicationUseCases: SearchMedicationUseCases,
    private val vibrateManager: VibrateManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchMedicinesUiState())
    val uiState: StateFlow<SearchMedicinesUiState> = _uiState.asStateFlow()

    private val queryFlow = MutableStateFlow("")

    init {
        syncMedications()

        viewModelScope.launch {
            queryFlow
                .debounce(300L)
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    _uiState.update { it.copy(isLoading = query.length >= 2) }
                    searchMedicationUseCases.searchMedication(query)
                        .catch { e ->
                            _uiState.update { it.copy(errorMessage = e.message, isLoading = false) }
                            emit(emptyList())
                        }
                }
                .collect { medicines ->
                    _uiState.update {
                        it.copy(results = medicines, isLoading = false, errorMessage = null)
                    }
                }
        }
    }

    fun syncMedications() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            searchMedicationUseCases.syncMedications()
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun onQueryChanged(query: String) {
        _uiState.update { it.copy(query = query) }
        queryFlow.value = query
    }

    fun onMedicineSelected(medication: Medication) {
        vibrateManager.vibrate(60L)
        _uiState.update { it.copy(selectedMedication = medication) }
    }

    fun onDismissDetail() {
        _uiState.update { it.copy(selectedMedication = null) }
    }

    fun onClearSearch() {
        _uiState.update { SearchMedicinesUiState() }
        queryFlow.value = ""
    }
}