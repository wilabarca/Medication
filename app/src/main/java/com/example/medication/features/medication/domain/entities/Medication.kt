package com.example.medication.features.medication.domain.entities

import com.example.medication.features.medication.data.datasources.remote.models.MedicationDto

data class Medication(
    val id: String,
    val name: String,
    val description: String,
    val quantity: Int,
    val price: Double
)
