package com.example.medication.features.patients.presentation.viewmodels

import com.example.medication.features.patients.domain.entities.Patient

data class PatientsUiState(
    val patients: List<Patient>   = emptyList(),
    val selectedPatient: Patient? = null,
    val isLoading: Boolean        = false,
    val successMessage: String?   = null,
    val errorMessage: String?     = null
)