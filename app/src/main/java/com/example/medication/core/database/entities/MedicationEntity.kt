package com.example.medication.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medications")
data class MedicationEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val name: String,
    val dosage: String,
    val form: String,
    val instructions: String? = null,
    val notes: String? = null,
    val quantity: Int,
    val price: Double? = null,
    val isActive: Boolean = true,
    val photoPath: String? = null
)