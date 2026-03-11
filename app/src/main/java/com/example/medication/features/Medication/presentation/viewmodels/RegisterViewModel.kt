package com.example.medication.features.Medication.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    // TODO: inyectar tu RegisterUseCase aquí cuando conectes la API
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

    private val _repetirContrasena = MutableStateFlow("")
    val repetirContrasena = _repetirContrasena.asStateFlow()

    fun setRepetirContrasena(value: String) {
        _repetirContrasena.value = value
    }

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    fun onCrearUsuario(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            if (_contrasena.value != _repetirContrasena.value) {
                _errorMessage.value = "Las contraseñas no coinciden"
                _isLoading.value = false
                return@launch
            }

            // TODO: reemplazar con tu llamada real a la API
            // val result = registerUseCase(usuario.value, contrasena.value)
            // if (result.isSuccess) onSuccess() else _errorMessage.value = "Error al registrar"

            _isLoading.value = false
        }
    }
}