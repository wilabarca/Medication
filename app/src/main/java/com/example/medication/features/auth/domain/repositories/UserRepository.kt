package com.example.medication.features.auth.domain.repositories

import android.provider.ContactsContract
import com.example.medication.features.auth.domain.entities.User

interface UserRepository {

    suspend fun login(
        email: String,
        password: String
    ): String

    suspend fun register(
        name: String,
        email: String,
        password: String
    ): User
}