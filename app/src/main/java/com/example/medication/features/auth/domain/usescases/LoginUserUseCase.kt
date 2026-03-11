package com.example.medication.features.auth.domain.usescases

import android.provider.ContactsContract
import com.example.medication.features.auth.domain.entities.User
import com.example.medication.features.auth.domain.repositories.UserRepository
import javax.inject.Inject

class LoginUserUseCase @Inject constructor(
    private val repository: UserRepository
) {

    suspend operator fun invoke(
        email: String,
        password: String
    ): User{
        return repository.login(email, password)
    }
}