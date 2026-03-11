package com.example.medication.features.auth.data.dataresources.remote.mapper

import com.example.medication.features.auth.data.dataresources.remote.models.UserDto
import com.example.medication.features.auth.domain.entities.User

fun UserDto.toDomain(): User{
    return User(
        id = id,
        name = name,
        email = email
    )
}