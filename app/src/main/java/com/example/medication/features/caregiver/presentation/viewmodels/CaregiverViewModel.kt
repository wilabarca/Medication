package com.example.medication.features.caregiver.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.example.medication.features.caregiver.domain.usescases.GetCaregiverHomeOptionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CaregiverViewModel @Inject constructor(
    private val getCaregiverHomeOptionsUseCase: GetCaregiverHomeOptionsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        CaregiverUiState(
            options = getCaregiverHomeOptionsUseCase()
        )
    )
    val uiState: StateFlow<CaregiverUiState> = _uiState.asStateFlow()
}