package com.example.medication.features.auth.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.medication.features.auth.presentation.components.RegisterForm
import com.example.medication.features.auth.presentation.viewmodels.AuthViewModel

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var usuario by rememberSaveable { mutableStateOf("") }
    var correo by rememberSaveable { mutableStateOf("") }
    var contrasena by rememberSaveable { mutableStateOf("") }
    var repetirContrasena by rememberSaveable { mutableStateOf("") }
    var role by rememberSaveable { mutableStateOf("caregiver") }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.registerSuccess) {
        if (uiState.registerSuccess) {
            viewModel.consumeRegisterSuccess()
            onRegisterSuccess()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1A1A2E),
                        Color(0xFF16213E),
                        Color(0xFF0F3460)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        RegisterForm(
            usuario = usuario,
            correo = correo,
            contrasena = contrasena,
            repetirContrasena = repetirContrasena,
            selectedRole = role,
            isLoading = uiState.isLoading,
            errorMessage = uiState.errorMessage,
            onUsuarioChange = {
                usuario = it
                if (uiState.errorMessage != null) viewModel.clearError()
            },
            onCorreoChange = {
                correo = it
                if (uiState.errorMessage != null) viewModel.clearError()
            },
            onContrasenaChange = {
                contrasena = it
                if (uiState.errorMessage != null) viewModel.clearError()
            },
            onRepetirContrasenaChange = {
                repetirContrasena = it
                if (uiState.errorMessage != null) viewModel.clearError()
            },
            onRoleChange = {
                role = it
                if (uiState.errorMessage != null) viewModel.clearError()
            },
            onCrearUsuario = {
                viewModel.register(
                    name = usuario,
                    email = correo,
                    password = contrasena,
                    repeatPassword = repetirContrasena,
                    role = role
                )
            }
        )
    }
}