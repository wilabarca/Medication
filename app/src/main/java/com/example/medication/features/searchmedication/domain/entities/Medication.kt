package com.example.medication.features.searchmedication.domain.entities

data class Medication(
    val id: String,
    val name: String,
    val description: String,
    val quantity: Int,
    val price: Double
)