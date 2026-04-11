package com.example.medication.features.medication.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medication.features.medication.domain.usecases.UpdateMedicationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EditMedicationUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val successMessage: String? = null,
    val error: String? = null
)

@HiltViewModel
class EditMedicationViewModel @Inject constructor(
    private val updateMedicationUseCase: UpdateMedicationUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditMedicationUiState())
    val uiState: StateFlow<EditMedicationUiState> = _uiState.asStateFlow()

    fun updateMedication(
        id: String,
        name: String,
        description: String,
        quantity: Int,
        price: Double,
        photoPath: String? = null  // ← agregar
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                isSuccess = false,
                error = null
            )
            try {
                updateMedicationUseCase(
                    id = id,
                    name = name,
                    description = description,
                    quantity = quantity,
                    price = price,
                    photoPath = photoPath  // ← agregar
                )
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isSuccess = true,
                    successMessage = "✅ Medicamento \"$name\" actualizado correctamente"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isSuccess = false,
                    error = "❌ Error al actualizar: ${e.message ?: "Error desconocido"}"
                )
            }
        }
    }

    fun resetState() {
        _uiState.value = EditMedicationUiState()
    }
}