package com.example.medication.features.auth.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Paleta médica/hospitalaria
private val MedBlue = Color(0xFF0077B6)
private val MedBlueDark = Color(0xFF003F5C)
private val MedTeal = Color(0xFF00B4D8)
private val Purple = Color(0xFF7B2CBF) // Añadido porque se usa en los íconos
private val TextColor = Color(0xFF023E58)
private val InputBackground = Color.White
private val InputBorderIdle = Color(0xFFB0D4E3)

@Composable
fun RegisterForm(
    usuario: String,
    correo: String,
    contrasena: String,
    repetirContrasena: String,
    selectedRole: String,
    isLoading: Boolean,
    errorMessage: String?,
    onUsuarioChange: (String) -> Unit,
    onCorreoChange: (String) -> Unit,
    onContrasenaChange: (String) -> Unit,
    onRepetirContrasenaChange: (String) -> Unit,
    onRoleChange: (String) -> Unit,
    onCrearUsuario: () -> Unit
) {
    var passwordVisible by remember { mutableStateOf(false) }
    var repeatPasswordVisible by remember { mutableStateOf(false) }
    val hasError = errorMessage != null

    Card(
        modifier = Modifier
            .fillMaxWidth(0.88f)
            .wrapContentHeight(),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F8FF))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp, vertical = 32.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "💊 Crear cuenta",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MedBlueDark
            )
            Text(
                text = "Regístrate para gestionar tus medicamentos",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Nombre
            OutlinedTextField(
                value = usuario,
                onValueChange = onUsuarioChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                label = { Text("Nombre completo", color = Color.Gray) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "Nombre",
                        tint = Purple
                    )
                },
                isError = hasError,
                shape = RoundedCornerShape(12.dp),
                colors = medFieldColors(hasError)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Correo
            OutlinedTextField(
                value = correo,
                onValueChange = onCorreoChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                label = { Text("Correo electrónico", color = Color.Gray) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Email,
                        contentDescription = "Correo electrónico",
                        tint = if (hasError) Color.Red else MedBlue
                    )
                },
                isError = hasError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                shape = RoundedCornerShape(12.dp),
                colors = medFieldColors(hasError)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Contraseña
            OutlinedTextField(
                value = contrasena,
                onValueChange = onContrasenaChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                label = { Text("Contraseña", color = Color.Gray) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = "Contraseña",
                        tint = Purple
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                        contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                        tint = Color.Gray,
                        modifier = Modifier.clickable { passwordVisible = !passwordVisible }
                    )
                },
                visualTransformation = if (passwordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                shape = RoundedCornerShape(12.dp),
                colors = medFieldColors(hasError)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Repetir contraseña
            OutlinedTextField(
                value = repetirContrasena,
                onValueChange = onRepetirContrasenaChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                label = { Text("Repetir contraseña", color = Color.Gray) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = "Repetir contraseña",
                        tint = Purple
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = if (repeatPasswordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                        contentDescription = if (repeatPasswordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                        tint = Color.Gray,
                        modifier = Modifier.clickable { repeatPasswordVisible = !repeatPasswordVisible }
                    )
                },
                visualTransformation = if (repeatPasswordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                isError = hasError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                shape = RoundedCornerShape(12.dp),
                colors = medFieldColors(hasError)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Selector de rol (de la rama Luis_conexion)
            RoleSelector(
                selectedRole = selectedRole,
                onRoleChange = onRoleChange
            )

            // Alerta de error animada (de la rama main)
            AnimatedVisibility(
                visible = hasError,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEB))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ErrorOutline,
                            contentDescription = null,
                            tint = Color.Red,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = errorMessage ?: "",
                            color = Color(0xFFB00020),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onCrearUsuario,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MedBlue),
                enabled = !isLoading,
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(22.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Crear cuenta",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun RoleSelector(
    selectedRole: String,
    onRoleChange: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = { onRoleChange("caregiver") },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (selectedRole == "caregiver") Purple else Color.Gray
            )
        ) {
            Text("Cuidador", color = Color.White)
        }

        Button(
            onClick = { onRoleChange("patient") },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (selectedRole == "patient") Purple else Color.Gray
            )
        ) {
            Text("Paciente", color = Color.White)
        }
    }
}

@Composable
private fun medFieldColors(hasError: Boolean) = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = Color(0xFF0077B6),
    unfocusedBorderColor = Color(0xFFB0D4E3),
    errorBorderColor = Color.Red,
    focusedContainerColor = Color.White,
    unfocusedContainerColor = Color.White,
    errorContainerColor = Color(0xFFFFF0F0),
    focusedTextColor = Color(0xFF023E58),
    unfocusedTextColor = Color(0xFF023E58),
    errorTextColor = Color(0xFF023E58),
    cursorColor = Color(0xFF0077B6)
)