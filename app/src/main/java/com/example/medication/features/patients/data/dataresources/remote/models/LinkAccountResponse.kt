package com.example.medication.features.patients.data.dataresources.remote.models

import com.google.gson.annotations.SerializedName

data class LinkAccountResponse(
    @SerializedName("message")   val message: String?,
    @SerializedName("success")   val success: Boolean?,
    @SerializedName("patientId") val patientId: String?   // ← lo que necesita el ViewModel
)