package com.example.medication.features.auth.domain.usescases

import com.example.medication.features.auth.domain.entities.User
import com.example.medication.features.auth.domain.repositories.UserRepository
import javax.inject.Inject


class RegisterUserUseCase @Inject constructor(
    private val repository: UserRepository
) {

    suspend operator fun invoke(
        name: String,
        email: String,
        password: String
    ): User {

        return repository.register(name, email, password)

    }

}