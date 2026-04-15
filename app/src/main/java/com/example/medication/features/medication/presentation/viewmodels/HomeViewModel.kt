package com.example.medication.features.medication.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medication.core.session.JwtSessionManager
import com.example.medication.features.medication.domain.entities.Medication
import com.example.medication.features.medication.domain.usecases.DeleteMedicationUseCase
import com.example.medication.features.medication.domain.usecases.GetMedicationUseCase
import com.example.medication.features.medication.domain.usecases.UpdateMedicationUseCase
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
    private val deleteMedicationUseCase: DeleteMedicationUseCase,
    private val updateMedicationUseCase: UpdateMedicationUseCase,
    private val jwtSessionManager: JwtSessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeMedicationUiState())
    val uiState: StateFlow<HomeMedicationUiState> = _uiState.asStateFlow()

    fun getMedications() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                val currentUserId = jwtSessionManager.getUserId()

                if (currentUserId.isNullOrBlank()) {
                    _uiState.value = _uiState.value.copy(
                        medications = emptyList(),
                        isLoading = false,
                        error = "No se pudo obtener el usuario actual desde el token"
                    )
                    return@launch
                }

                val medications = getMedicationUseCase()
                    .filter { it.userId == currentUserId }

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

    fun updateMedication(
        id: String,
        name: String,
        dosage: String,
        form: String,
        instructions: String?,
        notes: String?,
        quantity: Int,
        price: Double?,
        isActive: Boolean,
        photoPath: String? = null
    ) {
        viewModelScope.launch {
            try {
                val currentUserId = jwtSessionManager.getUserId()

                if (currentUserId.isNullOrBlank()) {
                    _uiState.value = _uiState.value.copy(
                        error = "No se pudo obtener el usuario actual desde el token"
                    )
                    return@launch
                }

                updateMedicationUseCase(
                    id = id,
                    userId = currentUserId,
                    name = name,
                    dosage = dosage,
                    form = form,
                    instructions = instructions,
                    notes = notes,
                    quantity = quantity,
                    price = price,
                    isActive = isActive,
                    photoPath = photoPath
                )

                getMedications()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Error al actualizar medicamento"
                )
            }
        }
    }
}