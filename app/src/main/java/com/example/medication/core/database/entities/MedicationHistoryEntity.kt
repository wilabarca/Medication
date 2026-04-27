package com.example.medication.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medication_history")
data class MedicationHistoryEntity(
    @PrimaryKey
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
    val deletedAt: Long = System.currentTimeMillis() // ← cuándo fue eliminado
)