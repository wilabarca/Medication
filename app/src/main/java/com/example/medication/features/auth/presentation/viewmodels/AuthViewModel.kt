package com.example.medication.features.auth.presentation.viewmodels

import android.content.Context
import android.provider.Settings
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medication.core.session.JwtSessionManager
import com.example.medication.features.auth.data.dataresources.remote.api.AuthApi
import com.example.medication.features.auth.data.dataresources.remote.models.RegisterDeviceRequest
import com.example.medication.features.auth.domain.entities.User
import com.example.medication.features.auth.domain.usescases.LoginUserUseCase
import com.example.medication.features.auth.domain.usescases.RegisterUserUseCase
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val loginSuccess: Boolean = false,
    val registerSuccess: Boolean = false,
    val loggedUser: User? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUserUseCase: LoginUserUseCase,
    private val registerUserUseCase: RegisterUserUseCase,
    private val jwtSessionManager: JwtSessionManager,
    private val authApi: AuthApi,
    @ApplicationContext private val context: Context
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
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            runCatching {
                loginUserUseCase(email.trim(), password)
            }.onSuccess { result ->
                jwtSessionManager.saveToken(result.token)

                registerCurrentDeviceAfterLogin()

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    loginSuccess = true,
                    loggedUser = result.user
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = error.message ?: "No se pudo iniciar sesión"
                )
            }
        }
    }

    private fun registerCurrentDeviceAfterLogin() {
        val userId = jwtSessionManager.getUserId()

        if (userId.isNullOrBlank()) {
            Log.e("FCM_DEBUG", "No se pudo obtener userId desde el JWT")
            return
        }

        val deviceId = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        )

        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.e("FCM_DEBUG", "No se pudo obtener token FCM", task.exception)
                    return@addOnCompleteListener
                }

                val fcmToken = task.result

                Log.d("FCM_DEBUG", "userId=$userId")
                Log.d("FCM_DEBUG", "deviceId=$deviceId")
                Log.d("FCM_DEBUG", "fcmToken=$fcmToken")

                viewModelScope.launch {
                    try {
                        authApi.registerDevice(
                            RegisterDeviceRequest(
                                userId = userId,
                                deviceId = deviceId,
                                fcmToken = fcmToken
                            )
                        )
                        Log.d("FCM_DEBUG", "Dispositivo registrado correctamente en backend")
                    } catch (e: Exception) {
                        Log.e("FCM_DEBUG", "Error registrando dispositivo", e)
                    }
                }
            }
    }

    fun register(
        name: String,
        email: String,
        password: String,
        repeatPassword: String,
        role: String
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

            role.isBlank() -> {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Selecciona un rol"
                )
                return
            }

            role != "caregiver" && role != "patient" -> {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Rol inválido"
                )
                return
            }
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            runCatching {
                registerUserUseCase(
                    name = name.trim(),
                    email = email.trim(),
                    password = password,
                    role = role
                )
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

    fun clearLoggedUser() {
        _uiState.value = _uiState.value.copy(loggedUser = null)
    }
}