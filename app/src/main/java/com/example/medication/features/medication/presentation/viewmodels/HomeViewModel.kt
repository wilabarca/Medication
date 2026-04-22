package com.example.medication.features.medication.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medication.core.hardware.domain.DeviceIdProvider
import com.example.medication.core.session.JwtSessionManager
import com.example.medication.features.medication.domain.entities.Medication
import com.example.medication.features.medication.domain.usecases.DeleteMedicationUseCase
import com.example.medication.features.medication.domain.usecases.GetMedicationsUseCase
import com.example.medication.features.medication.domain.usecases.UpdateMedicationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// ── UI State ───────────────────────────────────────────────────────────────────
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
    private val jwtSessionManager: JwtSessionManager,
    private val deviceIdProvider: DeviceIdProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        getMedications()
    }

    // ── Obtener medicamentos ───────────────────────────────────────────────────
    fun getMedications() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val patientId = jwtSessionManager.getUserId()
                if (patientId.isNullOrBlank()) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "No se pudo obtener el usuario desde el token"
                    )
                    return@launch
                }
                val medications = getMedicationsUseCase(patientId)
                _uiState.value = _uiState.value.copy(
                    medications = medications,
                    isLoading   = false,
                    error       = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error     = e.message ?: "Error al obtener medicamentos"
                )
            }
        }
    }

    // ── Eliminar medicamento ───────────────────────────────────────────────────
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

    // ── Actualizar medicamento ─────────────────────────────────────────────────
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
        startDate: String? = null,
        endDate: String? = null,
        photoPath: String? = null
    ) {
        viewModelScope.launch {
            try {
                val currentPatientId = jwtSessionManager.getUserId()
                if (currentPatientId.isNullOrBlank()) {
                    _uiState.value = _uiState.value.copy(
                        error = "No se pudo obtener el usuario actual desde el token"
                    )
                    return@launch
                }
                val deviceId = deviceIdProvider.getDeviceId()
                updateMedicationUseCase(
                    id           = id,
                    patientId    = currentPatientId,
                    name         = name,
                    dosage       = dosage,
                    form         = form,
                    instructions = instructions,
                    notes        = notes,
                    quantity     = quantity,
                    price        = price,
                    isActive     = isActive,
                    startDate    = startDate,
                    endDate      = endDate,
                    photoPath    = photoPath,
                    deviceId     = deviceId
                )
                getMedications()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Error al actualizar medicamento"
                )
            }
        }
    }

    // ── Limpiar error ──────────────────────────────────────────────────────────
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}