package com.example.medication.features.medication.data.datasources.remote.models

import com.google.gson.annotations.SerializedName

data class MedicationDto(
    @SerializedName("id")           val id: String?,
    @SerializedName("userId")       val userId: String?,
    @SerializedName("name")         val name: String?,
    @SerializedName("dosage")       val dosage: String?,
    @SerializedName("form")         val form: String?,
    @SerializedName("instructions") val instructions: String? = null,
    @SerializedName("notes")        val notes: String? = null,
    @SerializedName("quantity")     val quantity: Int?,
    @SerializedName("price")        val price: Any? = null,
    @SerializedName("isActive")     val isActive: Boolean? = true
)