package com.example.medication.features.auth.data.dataresources.remote.models

data class UpdateUserRequest(
    val name: String,
    val email: String,
    val password: String,
    val role: String
)
