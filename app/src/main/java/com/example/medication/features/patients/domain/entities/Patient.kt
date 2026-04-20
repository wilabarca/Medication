package com.example.medication.features.patients.domain.entities

data class Patient(
    val id: String,
    val caregiverUserId: String,
    val linkedUserId: String?,
    val name: String,
    val birthDate: String?,
    val relationship: String?,
    val notes: String?,
    val isActive: Boolean
)
