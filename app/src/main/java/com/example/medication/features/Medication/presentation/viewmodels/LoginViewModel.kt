package com.example.medication.features.Medication.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    // TODO: inyectar tu LoginUseCase aquí cuando conectes la API
    // private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _usuario = MutableStateFlow("")
    val usuario = _usuario.asStateFlow()

    fun setUsuario(value: String) {
        _usuario.value = value
    }

    private val _contrasena = MutableStateFlow("")
    val contrasena = _contrasena.asStateFlow()

    fun setContrasena(value: String) {
        _contrasena.value = value
    }

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    fun onIngresar(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            // TODO: reemplazar con tu llamada real a la API
            // val result = loginUseCase(usuario.value, contrasena.value)
            // if (result.isSuccess) onSuccess() else _errorMessage.value = "Credenciales incorrectas"

            _isLoading.value = false
        }
    }
}