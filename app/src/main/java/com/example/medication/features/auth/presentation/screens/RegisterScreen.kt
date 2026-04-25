package com.example.medication.features.auth.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.medication.features.auth.presentation.components.RegisterForm
import com.example.medication.features.auth.presentation.viewmodels.AuthViewModel

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var usuario           by rememberSaveable { mutableStateOf("") }
    var correo            by rememberSaveable { mutableStateOf("") }
    var contrasena        by rememberSaveable { mutableStateOf("") }
    var repetirContrasena by rememberSaveable { mutableStateOf("") }
    var selectedRole      by rememberSaveable { mutableStateOf("patient") }
    var showSuccessDialog by remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.registerSuccess) {
        if (uiState.registerSuccess) {
            showSuccessDialog = true
        }
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("Registro exitoso") },
            text  = { Text("Tu cuenta ha sido creada correctamente. Ya puedes iniciar sesión.") },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        viewModel.consumeRegisterSuccess()
                        onRegisterSuccess()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0077B6))
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
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            // ── Selector de rol ────────────────────────────────────
            androidx.compose.material3.Card(
                shape = RoundedCornerShape(16.dp),
                colors = androidx.compose.material3.CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.15f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        "¿Cómo vas a usar la app?",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(Modifier.height(12.dp))
                    Row {
                        FilterChip(
                            selected = selectedRole == "patient",
                            onClick  = { selectedRole = "patient" },
                            label    = { Text("Soy paciente") },
                            colors   = FilterChipDefaults.filterChipColors(
                                selectedContainerColor    = Color(0xFF0077B6),
                                selectedLabelColor        = Color.White,
                                containerColor            = Color.White.copy(alpha = 0.2f),
                                labelColor                = Color.White
                            )
                        )
                        Spacer(Modifier.width(12.dp))
                        FilterChip(
                            selected = selectedRole == "caregiver",
                            onClick  = { selectedRole = "caregiver" },
                            label    = { Text("Soy cuidador") },
                            colors   = FilterChipDefaults.filterChipColors(
                                selectedContainerColor    = Color(0xFF388E3C),
                                selectedLabelColor        = Color.White,
                                containerColor            = Color.White.copy(alpha = 0.2f),
                                labelColor                = Color.White
                            )
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // ── Formulario ─────────────────────────────────────────
            RegisterForm(
                usuario           = usuario,
                correo            = correo,
                contrasena        = contrasena,
                repetirContrasena = repetirContrasena,
                isLoading         = uiState.isLoading,
                errorMessage      = uiState.errorMessage,
                onUsuarioChange   = {
                    usuario = it
                    if (uiState.errorMessage != null) viewModel.clearError()
                },
                onCorreoChange    = {
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
                onCrearUsuario = {
                    viewModel.register(
                        name           = usuario,
                        email          = correo,
                        password       = contrasena,
                        repeatPassword = repetirContrasena,
                        role           = selectedRole
                    )
                }
            )
        }
    }
}