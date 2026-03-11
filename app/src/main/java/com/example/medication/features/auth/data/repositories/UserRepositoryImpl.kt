package com.example.medication.features.auth.data.repositories


import android.provider.ContactsContract
import com.example.medication.features.auth.data.dataresources.remote.api.AuthApi
import com.example.medication.features.auth.data.dataresources.remote.mapper.toDomain
import com.example.medication.features.auth.data.dataresources.remote.models.LoginRequest
import com.example.medication.features.auth.data.dataresources.remote.models.RegisterRequest
import com.example.medication.features.auth.domain.entities.User
import com.example.medication.features.auth.domain.repositories.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: AuthApi
    ): UserRepository {

    override  suspend fun login(
        email: String,
        password: String
    ): User {
        val response = api.login(
            LoginRequest(email, password)
        )
        return response.toDomain()
    }

    override suspend fun register(
        name: String,
        email: String,
        password: String
    ): User {
        val response = api.register(
            RegisterRequest(name, email, password)
        )
        return response.toDomain()
    }
}