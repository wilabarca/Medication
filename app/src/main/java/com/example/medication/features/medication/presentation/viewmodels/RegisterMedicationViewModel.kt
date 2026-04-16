package com.example.medication.features.medication.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medication.core.hardware.domain.DeviceIdProvider
import com.example.medication.core.session.JwtSessionManager
import com.example.medication.features.medication.domain.usecases.PostMedicationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RegisterMedicationUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val successMessage: String? = null,
    val error: String? = null
)

@HiltViewModel
class RegisterMedicationViewModel @Inject constructor(
    private val postMedicationUseCase: PostMedicationUseCase,
    private val jwtSessionManager: JwtSessionManager,
    private val deviceIdProvider: DeviceIdProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterMedicationUiState())
    val uiState: StateFlow<RegisterMedicationUiState> = _uiState.asStateFlow()

    fun registerMedication(
        name: String,
        dosage: String,
        form: String,
        instructions: String?,
        notes: String?,
        quantity: Int,
        price: Double?,
        isActive: Boolean = true,
        photoPath: String? = null
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                isSuccess = false,
                successMessage = null,
                error = null
            )

            try {
                val currentUserId = jwtSessionManager.getUserId()

                if (currentUserId.isNullOrBlank()) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isSuccess = false,
                        error = "No se pudo obtener el usuario actual desde el token"
                    )
                    return@launch
                }

                val deviceId = deviceIdProvider.getDeviceId()

                postMedicationUseCase(
                    userId = currentUserId,
                    name = name,
                    dosage = dosage,
                    form = form,
                    instructions = instructions,
                    notes = notes,
                    quantity = quantity,
                    price = price,
                    isActive = isActive,
                    photoPath = photoPath,
                    deviceId = deviceId
                )

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isSuccess = true,
                    successMessage = "✅ Medicamento \"$name\" registrado correctamente"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isSuccess = false,
                    error = "❌ Error al registrar: ${e.message ?: "Error desconocido"}"
                )
            }
        }
    }

    fun resetState() {
        _uiState.value = RegisterMedicationUiState()
    }
}