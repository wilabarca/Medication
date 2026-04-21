package com.example.medication.features.patients.presentation.viewmodels

import com.example.medication.features.patients.domain.entities.Patient

data class PatientsUiState(
    val isLoading: Boolean = false,
    val patients: List<Patient> = emptyList(),
    val selectedPatient: Patient? = null,
    val createdPatient: Patient? = null,
    val errorMessage: String? = null,
    val successMessage: String? = null
)
