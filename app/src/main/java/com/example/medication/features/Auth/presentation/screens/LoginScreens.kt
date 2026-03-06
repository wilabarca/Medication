package com.example.medication.features.Auth.presentation.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.medication.features.Auth.presentation.components.LoginForm

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onRegistrar: () -> Unit
) {
    var usuario by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A2E)),
        contentAlignment = Alignment.Center
    ) {
        LoginForm(
            usuario = usuario,
            contrasena = contrasena,
            isLoading = false,
            errorMessage = null,
            onUsuarioChange = { usuario = it },
            onContrasenaChange = { contrasena = it },
            onIngresar = {
                // TODO: conectar con ViewModel cuando tengas la API
                onLoginSuccess()
            },
            onRegistrar = onRegistrar
        )
    }
}