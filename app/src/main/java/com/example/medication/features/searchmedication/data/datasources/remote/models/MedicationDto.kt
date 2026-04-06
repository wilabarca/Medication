
package com.example.medication.features.searchmedication.data.remote.dto

import com.google.gson.annotations.SerializedName

data class MedicationDto(
    @SerializedName("id")          val id: String,
    @SerializedName("name")        val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("quantity")    val quantity: Int,
    @SerializedName("price")       val price: String  // ← String no Double
)