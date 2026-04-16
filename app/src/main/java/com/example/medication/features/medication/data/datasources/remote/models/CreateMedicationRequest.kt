package com.example.medication.features.medication.data.datasources.remote.models

data class CreateMedicationRequest(
    val userId: String,
    val name: String,
    val dosage: String,
    val form: String,
    val instructions: String? = null,
    val notes: String? = null,
    val quantity: Int,
    val price: Double? = null,
    val isActive: Boolean = true,
    val deviceId: String
)