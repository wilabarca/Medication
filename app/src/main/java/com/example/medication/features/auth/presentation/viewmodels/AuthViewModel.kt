package com.example.medication.features.auth.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medication.features.auth.domain.usescases.LoginUserUseCase
import com.example.medication.features.auth.domain.usescases.RegisterUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUserUseCase: LoginUserUseCase,
    private val registerUserUseCase: RegisterUserUseCase
) : ViewModel() {

    fun login(email: String, password: String) {

        viewModelScope.launch {

            val user = loginUserUseCase(email, password)

        }

    }

    fun register(name: String, email: String, password: String) {

        viewModelScope.launch {

            val user = registerUserUseCase(name, email, password)

        }

    }

}