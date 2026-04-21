package com.example.medication.features.caregiver.presentation.viewmodels

import com.example.medication.features.caregiver.domain.models.CaregiverMenuItem

data class CaregiverUiState(
    val isLoading: Boolean = false,
    val options: List<CaregiverMenuItem> = emptyList(),
    val  errorMessage: String? = null
)
