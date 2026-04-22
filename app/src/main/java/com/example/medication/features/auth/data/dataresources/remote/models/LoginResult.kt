package com.example.medication.features.auth.data.dataresources.remote.models

import com.example.medication.features.auth.domain.entities.User

data class LoginResult(
    val token: String,
    val user: User
)
