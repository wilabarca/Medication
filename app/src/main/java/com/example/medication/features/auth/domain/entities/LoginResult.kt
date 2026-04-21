package com.example.medication.features.auth.domain.entities

data class LoginResult(
    val token: String,
    val user: User
)
