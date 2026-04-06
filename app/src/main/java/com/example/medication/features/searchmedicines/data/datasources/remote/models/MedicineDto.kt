package com.example.medication.features.searchmedicines.data.remote.dto

import com.google.gson.annotations.SerializedName

data class MedicineDto(
    @SerializedName("id")                    val id: Int,
    @SerializedName("name")                  val name: String,
    @SerializedName("active_ingredient")     val activeIngredient: String,
    @SerializedName("presentation")          val presentation: String,
    @SerializedName("dosage")                val dosage: String,
    @SerializedName("requires_prescription") val requiresPrescription: Boolean,
    @SerializedName("description")           val description: String
)