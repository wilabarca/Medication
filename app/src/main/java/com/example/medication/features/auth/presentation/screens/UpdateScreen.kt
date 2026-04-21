package com.example.medication.features.auth.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.medication.features.auth.presentation.components.UpdateForm

@Composable
fun UpdateScreen(
    initialUsuario: String = "",
    initialCorreo: String = "",
    initialRole: String = "caregiver",
    onGuardarCambios: (String, String, String, String, String) -> Unit = { _, _, _, _, _ -> },
    onSeleccionarImagen: () -> Unit = {},
    onCancelar: () -> Unit = {}
) {
    var usuario by rememberSaveable { mutableStateOf(initialUsuario) }
    var correo by rememberSaveable { mutableStateOf(initialCorreo) }
    var contrasena by rememberSaveable { mutableStateOf("") }
    var repetirContrasena by rememberSaveable { mutableStateOf("") }
    var role by rememberSaveable { mutableStateOf(initialRole) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A2E)),
        contentAlignment = Alignment.Center
    ) {
        UpdateForm(
            usuario = usuario,
            correo = correo,
            contrasena = contrasena,
            repetirContrasena = repetirContrasena,
            selectedRole = role,
            isLoading = false,
            errorMessage = null,
            onUsuarioChange = { usuario = it },
            onCorreoChange = { correo = it },
            onContrasenaChange = { contrasena = it },
            onRepetirContrasenaChange = { repetirContrasena = it },
            onRoleChange = { role = it },
            onGuardarCambios = {
                onGuardarCambios(
                    usuario,
                    correo,
                    contrasena,
                    repetirContrasena,
                    role
                )
            },
            onSeleccionarImagen = onSeleccionarImagen,
            onCancelar = onCancelar
        )
    }
}