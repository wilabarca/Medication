package com.example.medication.features.history.domain.entities

data class MedicationHistory(
    val id: String,
    val patientId: String,
    val name: String,
    val dosage: String,
    val form: String,
    val instructions: String?,
    val notes: String?,
    val quantity: Int,
    val price: Double?,
    val isActive: Boolean,
    val startDate: String?,
    val endDate: String?,
    val photoPath: String?,
    val deletedAt: Long
)