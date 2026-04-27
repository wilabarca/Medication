package com.example.medication.features.medication.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medication.core.hardware.domain.DeviceIdProvider
import com.example.medication.features.medication.domain.usecases.PostMedicationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class RegisterMedicationUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val successMessage: String? = null,
    val error: String? = null
)

@HiltViewModel
class RegisterMedicationViewModel @Inject constructor(
    private val postMedicationUseCase: PostMedicationUseCase,
    private val deviceIdProvider: DeviceIdProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterMedicationUiState())
    val uiState: StateFlow<RegisterMedicationUiState> = _uiState.asStateFlow()

    fun registerMedication(
        patientId: String,
        name: String,
        dosage: String,
        form: String,
        instructions: String?,
        notes: String?,
        quantity: Int,
        price: Double?,
        isActive: Boolean = true,
        startDate: String? = null,
        endDate: String? = null,
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
                val deviceId = deviceIdProvider.getDeviceId()

                postMedicationUseCase(
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

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isSuccess = true,
                    successMessage = "✅ Medicamento \"$name\" registrado correctamente"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "❌ Error al registrar: ${e.message ?: "Error desconocido"}"
                )
            }
        }
    }

    fun resetState() {
        _uiState.value = RegisterMedicationUiState()
    }
}