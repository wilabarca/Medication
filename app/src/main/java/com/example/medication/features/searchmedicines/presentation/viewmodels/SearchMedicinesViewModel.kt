package com.example.medication.features.searchmedicines.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medication.features.searchmedicines.domain.entities.Medicine
import com.example.medication.features.searchmedicines.domain.usecases.SearchMedicinesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SearchMedicinesUiState(
    val query: String = "",
    val results: List<Medicine> = emptyList(),
    val isLoading: Boolean = false,
    val selectedMedicine: Medicine? = null,
    val errorMessage: String? = null
)

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchMedicinesViewModel @Inject constructor(
    private val searchMedicinesUseCase: SearchMedicinesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchMedicinesUiState())
    val uiState: StateFlow<SearchMedicinesUiState> = _uiState.asStateFlow()

    private val queryFlow = MutableStateFlow("")

    init {
        viewModelScope.launch {
            queryFlow
                .debounce(300L)
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    _uiState.update { it.copy(isLoading = query.length >= 2) }
                    searchMedicinesUseCase(query)
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

    fun onQueryChanged(query: String) {
        _uiState.update { it.copy(query = query) }
        queryFlow.value = query
    }

    fun onMedicineSelected(medicine: Medicine) {
        _uiState.update { it.copy(selectedMedicine = medicine) }
    }

    fun onDismissDetail() {
        _uiState.update { it.copy(selectedMedicine = null) }
    }

    fun onClearSearch() {
        _uiState.update { SearchMedicinesUiState() }
        queryFlow.value = ""
    }
}