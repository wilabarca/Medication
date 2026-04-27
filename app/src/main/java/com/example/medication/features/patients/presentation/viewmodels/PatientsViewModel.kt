package com.example.medication.features.patients.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medication.core.session.JwtSessionManager
import com.example.medication.features.patients.domain.repositories.PatientRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// ── PatientsUiState vive en su propio archivo PatientsUiState.kt ──────────────
// NO se redeclara aquí

@HiltViewModel
class PatientsViewModel @Inject constructor(
    private val patientRepository: PatientRepository,
    private val jwtSessionManager: JwtSessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(PatientsUiState())
    val uiState: StateFlow<PatientsUiState> = _uiState.asStateFlow()

    // ── Cargar pacientes ───────────────────────────────────────────────────────
    fun loadPatients(caregiverUserId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                val patients = patientRepository.getPatientsByCaregiver(caregiverUserId)
                _uiState.value = _uiState.value.copy(
                    patients  = patients,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading    = false,
                    errorMessage = e.message ?: "Error al cargar pacientes"
                )
            }
        }
    }

    // ── Crear paciente ─────────────────────────────────────────────────────────
    fun createPatient(
        name: String,
        birthDate: String?    = null,
        relationship: String? = null,
        notes: String?        = null,
        isActive: Boolean     = true
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                val caregiverId = jwtSessionManager.getUserId()
                if (caregiverId.isNullOrBlank()) {
                    _uiState.value = _uiState.value.copy(
                        isLoading    = false,
                        errorMessage = "No se pudo obtener el ID del cuidador"
                    )
                    return@launch
                }
                patientRepository.createPatient(
                    caregiverUserId = caregiverId,
                    linkedUserId    = null,
                    name            = name,
                    birthDate       = birthDate,
                    relationship    = relationship,
                    notes           = notes,
                    isActive        = isActive
                )
                _uiState.value = _uiState.value.copy(
                    isLoading      = false,
                    successMessage = "Paciente \"$name\" creado correctamente"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading    = false,
                    errorMessage = e.message ?: "Error al crear paciente"
                )
            }
        }
    }

    // ── Seleccionar paciente ───────────────────────────────────────────────────
    fun selectPatient(patient: com.example.medication.features.patients.domain.entities.Patient) {
        _uiState.value = _uiState.value.copy(selectedPatient = patient)
    }

    // ── Limpiar mensajes ───────────────────────────────────────────────────────
    fun clearMessages() {
        _uiState.value = _uiState.value.copy(
            successMessage = null,
            errorMessage   = null
        )
    }
}