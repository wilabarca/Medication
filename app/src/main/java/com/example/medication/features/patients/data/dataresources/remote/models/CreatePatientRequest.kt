package com.example.medication.features.patients.data.dataresources.remote.models

data class CreatePatientRequest(
    val caregiverUserId: String,
    val linkedUserId: String? = null,
    val name: String,
    val birthDate: String? = null,
    val relationship: String? = null,
    val notes: String? = null,
    val isActive: Boolean = true
)
