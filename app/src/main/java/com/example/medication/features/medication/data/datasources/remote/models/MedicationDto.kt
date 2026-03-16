package com.example.medication.features.medication.data.datasources.remote.models

data class MedicationDto(
    val id: String,
    val name: String,
    val description: String,
    val quantity: Int,
    val price: Double
)