package com.example.medication.features.searchmedicines.domain.entities

data class Medicine(
    val id: Int,
    val name: String,
    val activeIngredient: String,
    val presentation: String,
    val dosage: String,
    val requiresPrescription: Boolean,
    val description: String
)