package com.example.medication.features.medication.domain.entities

data class Medication(
    val id: String,
    val patientId: String,        // ← cambiado de userId a patientId
    val name: String,
    val dosage: String,
    val form: String,
    val instructions: String? = null,
    val notes: String? = null,
    val quantity: Int,
    val price: Double? = null,
    val isActive: Boolean = true,
    val startDate: String? = null, // ← nuevo
    val endDate: String? = null,   // ← nuevo
    val photoPath: String? = null  // ← local únicamente, no viene de la API
)