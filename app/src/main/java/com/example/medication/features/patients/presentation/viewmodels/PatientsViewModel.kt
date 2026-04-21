package com.example.medication.features.patients.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medication.features.patients.domain.entities.Patient
import com.example.medication.features.patients.domain.usecases.CreatePatientUseCase
import com.example.medication.features.patients.domain.usecases.DeletePatientUseCase
import com.example.medication.features.patients.domain.usecases.GetPatientByIdUseCase
import com.example.medication.features.patients.domain.usecases.GetPatientsByCaregiverUseCase
import com.example.medication.features.patients.domain.usecases.UpdatePatientUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class PatientsViewModel @Inject constructor(
    private val createPatientUseCase: CreatePatientUseCase,
    private val getPatientsByCaregiverUseCase: GetPatientsByCaregiverUseCase,
    private val getPatientByIdUseCase: GetPatientByIdUseCase,
    private val updatePatientUseCase: UpdatePatientUseCase,
    private val deletePatientUseCase: DeletePatientUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PatientsUiState())
    val uiState: StateFlow<PatientsUiState> = _uiState.asStateFlow()

    fun loadPatients(caregiverUserId: String) {
        if (caregiverUserId.isBlank()) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "caregiverUserId inválido"
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            runCatching {
                getPatientsByCaregiverUseCase(caregiverUserId)
            }.onSuccess { patients ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    patients = patients
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = error.message ?: "No se pudieron cargar los pacientes"
                )
            }
        }
    }

    fun createPatient(
        caregiverUserId: String,
        linkedUserId: String? = null,
        name: String,
        birthDate: String? = null,
        relationship: String? = null,
        notes: String? = null,
        isActive: Boolean = true
    ) {
        if (caregiverUserId.isBlank() || name.isBlank()) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "El nombre y caregiverUserId son obligatorios"
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                successMessage = null
            )

            runCatching {
                createPatientUseCase(
                    caregiverUserId = caregiverUserId,
                    linkedUserId = linkedUserId,
                    name = name.trim(),
                    birthDate = birthDate,
                    relationship = relationship,
                    notes = notes,
                    isActive = isActive
                )
            }.onSuccess { patient ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    createdPatient = patient,
                    successMessage = "Paciente creado correctamente"
                )

                loadPatients(caregiverUserId)
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = error.message ?: "No se pudo crear el paciente"
                )
            }
        }
    }

    fun getPatientById(id: String) {
        if (id.isBlank()) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Id inválido"
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            runCatching {
                getPatientByIdUseCase(id)
            }.onSuccess { patient ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    selectedPatient = patient
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = error.message ?: "No se pudo obtener el paciente"
                )
            }
        }
    }

    fun selectPatient(patient: Patient) {
        _uiState.value = _uiState.value.copy(
            selectedPatient = patient
        )
    }

    fun updatePatient(patient: Patient) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                successMessage = null
            )

            runCatching {
                updatePatientUseCase(patient)
            }.onSuccess { updatedPatient ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    selectedPatient = updatedPatient,
                    successMessage = "Paciente actualizado correctamente"
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = error.message ?: "No se pudo actualizar el paciente"
                )
            }
        }
    }

    fun deletePatient(id: String, caregiverUserId: String) {
        if (id.isBlank()) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Id inválido"
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                successMessage = null
            )

            runCatching {
                deletePatientUseCase(id)
            }.onSuccess {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    successMessage = "Paciente eliminado correctamente",
                    selectedPatient = null
                )

                loadPatients(caregiverUserId)
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = error.message ?: "No se pudo eliminar el paciente"
                )
            }
        }
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null,
            successMessage = null
        )
    }

    fun clearCreatedPatient() {
        _uiState.value = _uiState.value.copy(
            createdPatient = null
        )
    }
}