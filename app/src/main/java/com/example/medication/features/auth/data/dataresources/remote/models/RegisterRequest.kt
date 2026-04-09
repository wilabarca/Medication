package com.example.medication.features.auth.data.dataresources.remote.models

data class  RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)
