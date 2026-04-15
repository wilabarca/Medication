package com.example.medication.features.auth.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medication.core.session.JwtSessionManager
import com.example.medication.features.auth.domain.usescases.LoginUserUseCase
import com.example.medication.features.auth.domain.usescases.RegisterUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val loginSuccess: Boolean = false,
    val registerSuccess: Boolean = false
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUserUseCase: LoginUserUseCase,
    private val registerUserUseCase: RegisterUserUseCase,
    private val jwtSessionManager: JwtSessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Completa correo y contraseña"
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)

            runCatching {
                loginUserUseCase(email.trim(), password)
            }.onSuccess { token ->
                jwtSessionManager.saveToken(token)

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    loginSuccess = true
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = error.message ?: "No se pudo iniciar sesión"
                )
            }
        }
    }

    fun register(
        name: String,
        email: String,
        password: String,
        repeatPassword: String
    ) {
        when {
            name.isBlank() || email.isBlank() || password.isBlank() || repeatPassword.isBlank() -> {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Completa todos los campos"
                )
                return
            }

            password != repeatPassword -> {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Las contraseñas no coinciden"
                )
                return
            }
        }

        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)

            runCatching {
                registerUserUseCase(name.trim(), email.trim(), password)
            }.onSuccess {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    registerSuccess = true
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = error.message ?: "No se pudo registrar el usuario"
                )
            }
        }
    }

    fun consumeLoginSuccess() {
        _uiState.value = _uiState.value.copy(loginSuccess = false)
    }

    fun consumeRegisterSuccess() {
        _uiState.value = _uiState.value.copy(registerSuccess = false)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}