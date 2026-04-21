package com.example.medication.features.auth.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
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
    var showSuccessDialog by remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.registerSuccess) {
        if (uiState.registerSuccess) {
            showSuccessDialog = true
        }
    }

    // Diálogo de éxito
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("Registro exitoso") },
            text = { Text("Tu cuenta ha sido creada correctamente. Ya puedes iniciar sesión.") },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        viewModel.consumeRegisterSuccess()
                        onRegisterSuccess()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0077B6)
                    )
                ) {
                    Text("Continuar", color = Color.White)
                }
            }
        )
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