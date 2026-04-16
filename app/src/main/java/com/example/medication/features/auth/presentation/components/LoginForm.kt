package com.example.medication.features.auth.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Purple = Color(0xFF6650A4)
private val TextColor = Color(0xFF1A1A2E)
private val InputBackground = Color.White
private val InputBorderIdle = Color(0xFFCCCCCC)

@Composable
fun LoginForm(
    usuario: String,
    contrasena: String,
    isLoading: Boolean,
    errorMessage: String?,
    onUsuarioChange: (String) -> Unit,
    onContrasenaChange: (String) -> Unit,
    onIngresar: () -> Unit,
    onRegistrar: () -> Unit
) {
    var passwordVisible by remember { mutableStateOf(false) }
    val hasError = errorMessage != null

    Card(
        modifier = Modifier
            .fillMaxWidth(0.88f)
            .wrapContentHeight(),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8F8))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp, vertical = 36.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Título
            Text(
                text = "Bienvenido",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = TextColor
            )
            Text(
                text = "Inicia sesión para continuar",
                fontSize = 13.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Campo email
            OutlinedTextField(
                value = usuario,
                onValueChange = onUsuarioChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = {
                    Text("Correo electrónico", color = Color.Gray)
                },
                label = { Text("Correo electrónico", color = Color.Gray) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Email,
                        contentDescription = null,
                        tint = if (hasError) Color.Red else Purple
                    )
                },
                isError = hasError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Purple,
                    unfocusedBorderColor = InputBorderIdle,
                    errorBorderColor = Color.Red,
                    focusedContainerColor = InputBackground,
                    unfocusedContainerColor = InputBackground,
                    errorContainerColor = Color(0xFFFFF0F0),
                    focusedTextColor = TextColor,
                    unfocusedTextColor = TextColor,
                    errorTextColor = TextColor,
                    cursorColor = Purple
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo contraseña
            OutlinedTextField(
                value = contrasena,
                onValueChange = onContrasenaChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = { Text("Contraseña", color = Color.Gray) },
                label = { Text("Contraseña", color = Color.Gray) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = null,
                        tint = if (hasError) Color.Red else Purple
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                        contentDescription = if (passwordVisible) "Ocultar" else "Mostrar",
                        tint = Color.Gray,
                        modifier = Modifier.clickable { passwordVisible = !passwordVisible }
                    )
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                isError = hasError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Purple,
                    unfocusedBorderColor = InputBorderIdle,
                    errorBorderColor = Color.Red,
                    focusedContainerColor = InputBackground,
                    unfocusedContainerColor = InputBackground,
                    errorContainerColor = Color(0xFFFFF0F0),
                    focusedTextColor = TextColor,
                    unfocusedTextColor = TextColor,
                    errorTextColor = TextColor,
                    cursorColor = Purple
                )
            )

            // Alerta de error animada
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

            // Botón ingresar
            Button(
                onClick = onIngresar,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Purple),
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
                        text = "Ingresar",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Registro
            Row {
                Text(
                    text = "¿No tienes cuenta? ",
                    fontSize = 13.sp,
                    color = Color.DarkGray
                )
                Text(
                    text = "Registrarse",
                    fontSize = 13.sp,
                    color = Purple,
                    fontWeight = FontWeight.SemiBold,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable { onRegistrar() }
                )
            }
        }
    }
}