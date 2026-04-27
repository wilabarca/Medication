package com.example.medication.features.patients.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medication.features.patients.domain.entities.Patient
import com.example.medication.features.patients.domain.repositories.PatientRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PatientsViewModel @Inject constructor(
    private val patientRepository: PatientRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PatientsUiState())
    val uiState: StateFlow<PatientsUiState> = _uiState.asStateFlow()

    fun loadPatients(caregiverUserId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            try {
                val patients = patientRepository.getPatientsByCaregiver(caregiverUserId)

                _uiState.value = _uiState.value.copy(
                    patients = patients,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Error al cargar pacientes"
                )
            }
        }
    }

    fun generateLinkToken(
        patientId: String,
        caregiverUserId: String
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isGeneratingToken = true,
                errorMessage = null,
                linkToken = null,
                linkTokenExpiresAt = null
            )

            try {
                val response = patientRepository.generateLinkToken(
                    patientId = patientId,
                    caregiverUserId = caregiverUserId
                )

                _uiState.value = _uiState.value.copy(
                    isGeneratingToken = false,
                    linkToken = response.token,
                    linkTokenExpiresAt = response.expiresAt
                )
            } catch (error: Exception) {
                _uiState.value = _uiState.value.copy(
                    isGeneratingToken = false,
                    errorMessage = error.message ?: "Error al generar código de vinculación"
                )
            }
        }
    }

    fun clearLinkToken() {
        _uiState.value = _uiState.value.copy(
            linkToken = null,
            linkTokenExpiresAt = null,
            isGeneratingToken = false
        )
    }

    fun createPatient(
        caregiverUserId: String,
        name: String,
        birthDate: String? = null,
        relationship: String? = null,
        notes: String? = null,
        isActive: Boolean = true
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            try {
                if (caregiverUserId.isBlank()) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "No se pudo obtener el ID del cuidador"
                    )
                    return@launch
                }

                patientRepository.createPatient(
                    caregiverUserId = caregiverUserId,
                    linkedUserId = null,
                    name = name,
                    birthDate = birthDate,
                    relationship = relationship,
                    notes = notes,
                    isActive = isActive
                )

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    successMessage = "Paciente \"$name\" creado correctamente"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Error al crear paciente"
                )
            }
        }
    }

    fun selectPatient(patient: Patient) {
        _uiState.value = _uiState.value.copy(
            selectedPatient = patient
        )
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(
            successMessage = null,
            errorMessage = null
        )
    }
}