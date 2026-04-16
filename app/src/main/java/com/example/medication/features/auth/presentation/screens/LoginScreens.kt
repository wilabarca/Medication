package com.example.medication.features.auth.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.medication.features.auth.presentation.components.LoginForm
import com.example.medication.features.auth.presentation.viewmodels.AuthViewModel

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onRegistrar: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var usuario by rememberSaveable { mutableStateOf("") }
    var contrasena by rememberSaveable { mutableStateOf("") }

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.loginSuccess) {
        if (uiState.loginSuccess) {
            viewModel.consumeLoginSuccess()
            onLoginSuccess()
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
        LoginForm(
            usuario = usuario,
            contrasena = contrasena,
            isLoading = uiState.isLoading,
            errorMessage = uiState.errorMessage,
            onUsuarioChange = {
                usuario = it
                if (uiState.errorMessage != null) viewModel.clearError()
            },
            onContrasenaChange = {
                contrasena = it
                if (uiState.errorMessage != null) viewModel.clearError()
            },
            onIngresar = {
                viewModel.login(usuario, contrasena)
            },
            onRegistrar = onRegistrar
        )
    }
}