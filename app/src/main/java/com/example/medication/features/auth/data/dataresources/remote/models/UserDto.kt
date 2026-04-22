package com.example.medication.features.auth.data.dataresources.remote.models

data class UserDto(
    val id: String?,     // ← todos nullable
    val name: String?,
    val email: String?,
    val role: String?
)