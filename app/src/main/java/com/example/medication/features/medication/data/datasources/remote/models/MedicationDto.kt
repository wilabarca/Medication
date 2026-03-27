package com.example.medication.features.medication.data.datasources.remote.models

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName

data class MedicationDto(
    val id: String?,
    val name: String?,
    val description: String?,
    val quantity: Int?,
    val price: Any?
)