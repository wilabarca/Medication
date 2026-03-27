package com.example.medication.features.medication.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    val error: String? = null
)

@HiltViewModel
class RegisterMedicationViewModel @Inject constructor(
    private val postMedicationUseCase: PostMedicationUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterMedicationUiState())
    val uiState: StateFlow<RegisterMedicationUiState> = _uiState.asStateFlow()

    fun registerMedication(
        name: String,
        description: String,
        quantity: Int,
        price: Double
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                isSuccess = false,
                error = null
            )

            try {
                postMedicationUseCase(
                    name = name,
                    description = description,
                    quantity = quantity,
                    price = price
                )

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isSuccess = true
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isSuccess = false,
                    error = e.message ?: "Error al registrar medicamento"
                )
            }
        }
    }

    fun resetState() {
        _uiState.value = RegisterMedicationUiState()
    }
}