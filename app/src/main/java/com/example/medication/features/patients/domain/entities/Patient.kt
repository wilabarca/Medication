package com.example.medication.features.patients.domain.entities

import com.example.medication.features.medication.domain.entities.Medication

data class Patient(
    val id: String,
    val caregiverUserId: String,
    val linkedUserId: String?,
    val name: String,
    val birthDate: String?,
    val relationship: String?,
    val notes: String?,
    val isActive: Boolean,
    val medications: List<Medication> = emptyList()  // ← nuevo, default vacío
)