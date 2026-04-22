package com.example.medication.features.medication.data.datasources.remote.models

import com.google.gson.annotations.SerializedName

data class CreateMedicationRequest(
    @SerializedName("patientId")    val patientId: String,
    @SerializedName("name")         val name: String,
    @SerializedName("dosage")       val dosage: String,
    @SerializedName("form")         val form: String,
    @SerializedName("instructions") val instructions: String? = null,
    @SerializedName("notes")        val notes: String? = null,
    @SerializedName("quantity")     val quantity: Int,
    @SerializedName("price")        val price: Double? = null,
    @SerializedName("isActive")     val isActive: Boolean = true,
    @SerializedName("startDate")    val startDate: String? = null,
    @SerializedName("endDate")      val endDate: String? = null,
    @SerializedName("deviceId")     val deviceId: String
)