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
import com.example.medication.features.auth.presentation.components.LoginForm
import com.example.medication.features.auth.presentation.viewmodels.AuthViewModel

@Composable
fun LoginScreen(
    onCaregiverLoginSuccess: () -> Unit,
    onPatientLoginSuccess: () -> Unit,
    onRegistrar: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var usuario   by rememberSaveable { mutableStateOf("") }
    var contrasena by rememberSaveable { mutableStateOf("") }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.loginSuccess, uiState.loggedUser) {
        val loggedUser = uiState.loggedUser
        if (uiState.loginSuccess && loggedUser != null) {
            viewModel.consumeLoginSuccess()
            viewModel.clearLoggedUser()
            when (loggedUser.role) {
                "caregiver" -> onCaregiverLoginSuccess()
                "patient"   -> onPatientLoginSuccess()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF003F5C),
                        Color(0xFF0077B6),
                        Color(0xFF00B4D8)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        LoginForm(
            usuario      = usuario,
            contrasena   = contrasena,
            isLoading    = uiState.isLoading,
            errorMessage = uiState.errorMessage,
            onUsuarioChange = {
                usuario = it
                if (uiState.errorMessage != null) viewModel.clearError()
            },
            onContrasenaChange = {
                contrasena = it
                if (uiState.errorMessage != null) viewModel.clearError()
            },
            onIngresar  = { viewModel.login(usuario, contrasena) },
            onRegistrar = onRegistrar
        )
    }
}