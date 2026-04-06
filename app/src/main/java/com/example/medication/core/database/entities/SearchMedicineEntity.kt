package com.example.medication.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_medicines_cache")
data class SearchMedicineEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val activeIngredient: String,
    val presentation: String,
    val dosage: String,
    val requiresPrescription: Boolean,
    val description: String,
    val cachedAt: Long = System.currentTimeMillis()
)