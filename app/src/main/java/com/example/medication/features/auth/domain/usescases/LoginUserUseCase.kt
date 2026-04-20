package com.example.medication.features.auth.domain.usescases


import com.example.medication.features.auth.domain.entities.LoginResult
import com.example.medication.features.auth.domain.repositories.UserRepository
import javax.inject.Inject

class LoginUserUseCase @Inject constructor(
    private val repository: UserRepository
) {

    suspend operator fun invoke(
        email: String,
        password: String
    ): LoginResult {
        return repository.login(email, password)
    }
}