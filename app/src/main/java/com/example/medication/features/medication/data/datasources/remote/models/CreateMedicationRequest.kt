package com.example.medication.features.medication.data.datasources.remote.models

data class CreateMedicationRequest(
    val name: String,
    val description: String,
    val quantity: Int,
    val price: Double
    // photoPath no se envía a la API
)