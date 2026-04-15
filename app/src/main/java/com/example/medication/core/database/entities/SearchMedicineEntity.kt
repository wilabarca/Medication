package com.example.medication.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_medicines_cache")
data class SearchMedicineEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String?, // ← nullable
    val quantity: Int,
    val price: Double,
    val cachedAt: Long = System.currentTimeMillis()
)