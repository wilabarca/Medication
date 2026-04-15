package com.example.medication.features.auth.data.dataresources.remote.models

data class RegisterDeviceRequest(
    val userId: String,
    val deviceId: String,
    val fcmToken: String
)
