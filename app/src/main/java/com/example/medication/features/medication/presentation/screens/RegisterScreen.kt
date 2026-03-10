package com.example.medication.features.medication.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.medication.features.medication.presentation.components.RegisterForm

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit
) {
    var usuario by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var repetirContrasena by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A2E)),
        contentAlignment = Alignment.Center
    ) {
        RegisterForm(
            usuario = usuario,
            contrasena = contrasena,
            repetirContrasena = repetirContrasena,
            isLoading = false,
            errorMessage = null,
            onUsuarioChange = { usuario = it },
            onContrasenaChange = { contrasena = it },
            onRepetirContrasenaChange = { repetirContrasena = it },
            onCrearUsuario = {
                // TODO: conectar con ViewModel cuando tengas la API
                onRegisterSuccess()
            }
        )
    }
}