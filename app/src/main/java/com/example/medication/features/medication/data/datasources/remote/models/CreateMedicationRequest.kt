package com.example.medication.features.medication.data.datasources.remote.models

data class CreateMedicationRequest(
    val patientId: String,     // ← userId → patientId
    val name: String,
    val dosage: String,
    val form: String,
    val instructions: String? = null,
    val notes: String? = null,
    val quantity: Int,
    val price: Double? = null,
    val isActive: Boolean = true,
    val startDate: String? = null,  // ← nuevo
    val endDate: String? = null,    // ← nuevo
    val deviceId: String
)