package com.example.medication.features.medication.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medication.features.medication.domain.entities.Medication
import com.example.medication.features.medication.domain.usecases.DeleteMedicationUseCase
import com.example.medication.features.medication.domain.usecases.GetMedicationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject




data class HomeMedicationUiState(
    val medications: List<Medication> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMedicationUseCase: GetMedicationUseCase,
    private val deleteMedicationUseCase: DeleteMedicationUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeMedicationUiState())
    val uiState: StateFlow<HomeMedicationUiState> = _uiState.asStateFlow()

    fun getMedications() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            try {
                val medications = getMedicationUseCase()
                _uiState.value = _uiState.value.copy(
                    medications = medications,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error al obtener medicamentos"
                )
            }
        }
    }

    fun deleteMedication(id: String) {
        viewModelScope.launch {
            try {
                deleteMedicationUseCase(id)
                getMedications()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Error al eliminar medicamento"
                )
            }
        }
    }
}