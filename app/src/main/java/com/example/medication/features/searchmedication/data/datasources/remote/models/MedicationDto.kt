package com.example.medication.features.searchmedication.data.datasources.remote.models

import com.google.gson.annotations.SerializedName

data class MedicationDto(
    @SerializedName("id")          val id: String,
    @SerializedName("name")        val name: String,
    @SerializedName("description") val description: String?, // ← nullable
    @SerializedName("quantity")    val quantity: Int,
    @SerializedName("price")       val price: String
)