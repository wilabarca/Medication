package com.example.medication.features.medication.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medication.core.hardware.domain.DeviceIdProvider
import com.example.medication.features.medication.domain.entities.Medication
import com.example.medication.features.medication.domain.usecases.DeleteMedicationUseCase
import com.example.medication.features.medication.domain.usecases.GetMedicationsUseCase
import com.example.medication.features.medication.domain.usecases.UpdateMedicationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomeUiState(
    val medications: List<Medication> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMedicationsUseCase: GetMedicationsUseCase,
    private val deleteMedicationUseCase: DeleteMedicationUseCase,
    private val updateMedicationUseCase: UpdateMedicationUseCase,
    private val deviceIdProvider: DeviceIdProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun getMedications(patientId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                val medications = getMedicationsUseCase(patientId)

                _uiState.value = _uiState.value.copy(
                    medications = medications,
                    isLoading = false,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error al obtener medicamentos"
                )
            }
        }
    }

    fun deleteMedication(id: String, patientId: String) {
        viewModelScope.launch {
            try {
                deleteMedicationUseCase(id)
                getMedications(patientId)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Error al eliminar medicamento"
                )
            }
        }
    }

    fun updateMedication(
        id: String,
        patientId: String,
        name: String,
        dosage: String,
        form: String,
        instructions: String?,
        notes: String?,
        quantity: Int,
        price: Double?,
        isActive: Boolean,
        startDate: String? = null,
        endDate: String? = null,
        photoPath: String? = null
    ) {
        viewModelScope.launch {
            try {
                val deviceId = deviceIdProvider.getDeviceId()

                updateMedicationUseCase(
                    id = id,
                    patientId = patientId,
                    name = name,
                    dosage = dosage,
                    form = form,
                    instructions = instructions,
                    notes = notes,
                    quantity = quantity,
                    price = price,
                    isActive = isActive,
                    startDate = startDate,
                    endDate = endDate,
                    photoPath = photoPath,
                    deviceId = deviceId
                )

                getMedications(patientId)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Error al actualizar medicamento"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}