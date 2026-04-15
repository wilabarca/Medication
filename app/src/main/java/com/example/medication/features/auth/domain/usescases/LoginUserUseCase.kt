package com.example.medication.features.auth.domain.usescases


import com.example.medication.features.auth.domain.repositories.UserRepository
import javax.inject.Inject

class LoginUserUseCase @Inject constructor(
    private val repository: UserRepository
) {

    suspend operator fun invoke(
        email: String,
        password: String
    ): String{
        return repository.login(email, password)
    }
}