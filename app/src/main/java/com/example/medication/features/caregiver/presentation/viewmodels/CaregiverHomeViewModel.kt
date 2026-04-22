package com.example.medication.features.caregiver.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medication.core.session.JwtSessionManager
import com.example.medication.features.patients.domain.entities.Patient
import com.example.medication.features.patients.domain.usecases.GetPatientsByCaregiversUseCase
import com.example.medication.features.caregiver.domain.usecases.GenerateLinkTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CaregiverHomeUiState(
    val patients: List<Patient>    = emptyList(),
    val isLoading: Boolean         = false,
    val isGeneratingToken: Boolean = false,
    val linkToken: String?         = null,
    val snackbarMessage: String?   = null,
    val error: String?             = null
)

@HiltViewModel
class CaregiverHomeViewModel @Inject constructor(
    private val getPatientsByCaregiversUseCase: GetPatientsByCaregiversUseCase,
    private val generateLinkTokenUseCase: GenerateLinkTokenUseCase,
    private val jwtSessionManager: JwtSessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(CaregiverHomeUiState())
    val uiState: StateFlow<CaregiverHomeUiState> = _uiState.asStateFlow()

    init {
        loadPatients()
    }

    // ── Cargar pacientes del cuidador ──────────────────────────────────────────
    fun loadPatients() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val caregiverId = jwtSessionManager.getUserId()
                if (caregiverId.isNullOrBlank()) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error     = "No se pudo obtener el ID del cuidador"
                    )
                    return@launch
                }
                val patients = getPatientsByCaregiversUseCase(caregiverId)
                _uiState.value = _uiState.value.copy(
                    patients  = patients,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error     = e.message ?: "Error al cargar pacientes"
                )
            }
        }
    }

    // ── Generar token de vinculación ───────────────────────────────────────────
    fun generateLinkToken() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isGeneratingToken = true)
            try {
                val caregiverId = jwtSessionManager.getUserId()
                if (caregiverId.isNullOrBlank()) {
                    _uiState.value = _uiState.value.copy(
                        isGeneratingToken = false,
                        snackbarMessage   = "No se pudo obtener el ID del cuidador"
                    )
                    return@launch
                }
                val token = generateLinkTokenUseCase(caregiverId)
                _uiState.value = _uiState.value.copy(
                    isGeneratingToken = false,
                    linkToken         = token
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isGeneratingToken = false,
                    snackbarMessage   = "Error al generar token: ${e.message}"
                )
            }
        }
    }

    fun clearToken() {
        _uiState.value = _uiState.value.copy(linkToken = null)
    }

    fun clearSnackbar() {
        _uiState.value = _uiState.value.copy(snackbarMessage = null)
    }
}