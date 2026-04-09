package com.example.medication.features.auth.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Purple = Color(0xFF6650A4)
private val CardBackground = Color(0xFFF2F2F2)
private val InputBackground = Color(0xFFF7F7F7)
private val BorderColor = Color(0xFFD6D6D6)

@Composable
fun UpdateForm(
    usuario: String,
    correo: String,
    contrasena: String,
    repetirContrasena: String,
    isLoading: Boolean,
    errorMessage: String?,
    onUsuarioChange: (String) -> Unit,
    onCorreoChange: (String) -> Unit,
    onContrasenaChange: (String) -> Unit,
    onRepetirContrasenaChange: (String) -> Unit,
    onGuardarCambios: () -> Unit,
    onSeleccionarImagen: () -> Unit,
    onCancelar: () -> Unit
) {
    var passwordVisible by remember { mutableStateOf(false) }
    var repeatPasswordVisible by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth(0.72f)
            .wrapContentHeight(),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 22.dp, vertical = 26.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfilePicker(
                onSeleccionarImagen = onSeleccionarImagen
            )

            Spacer(modifier = Modifier.height(18.dp))

            UpdateInput(
                value = usuario,
                onValueChange = onUsuarioChange,
                placeholder = "Usuario",
                leadingIcon = Icons.Filled.Person,
                contentDescription = "Usuario"
            )

            Spacer(modifier = Modifier.height(12.dp))

            UpdateInput(
                value = correo,
                onValueChange = onCorreoChange,
                placeholder = "Email",
                leadingIcon = Icons.Filled.Email,
                contentDescription = "Email",
                keyboardType = KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(12.dp))

            UpdateInput(
                value = contrasena,
                onValueChange = onContrasenaChange,
                placeholder = "Contraseña",
                leadingIcon = Icons.Filled.Lock,
                contentDescription = "Contraseña",
                keyboardType = KeyboardType.Password,
                isPassword = true,
                passwordVisible = passwordVisible,
                onTogglePasswordVisibility = {
                    passwordVisible = !passwordVisible
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            UpdateInput(
                value = repetirContrasena,
                onValueChange = onRepetirContrasenaChange,
                placeholder = "Repetir Contraseña",
                leadingIcon = Icons.Filled.Lock,
                contentDescription = "Repetir Contraseña",
                keyboardType = KeyboardType.Password,
                isPassword = true,
                passwordVisible = repeatPasswordVisible,
                onTogglePasswordVisibility = {
                    repeatPasswordVisible = !repeatPasswordVisible
                }
            )

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            Button(
                onClick = onGuardarCambios,
                modifier = Modifier
                    .fillMaxWidth(0.58f)
                    .height(34.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Purple),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Actualizar todo",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            TextButton(
                onClick = onCancelar
            ) {
                Text(
                    text = "Cancelar",
                    color = Purple,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
private fun ProfilePicker(
    onSeleccionarImagen: () -> Unit
) {
    Box(
        contentAlignment = Alignment.BottomEnd
    ) {
        Surface(
            modifier = Modifier.size(74.dp),
            shape = CircleShape,
            color = CardBackground,
            tonalElevation = 0.dp,
            shadowElevation = 0.dp
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(74.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Foto de perfil",
                    tint = Color.Black,
                    modifier = Modifier.size(58.dp)
                )
            }
        }

        Box(
            modifier = Modifier
                .offset(x = 4.dp, y = 2.dp)
                .size(24.dp)
                .clip(CircleShape)
                .background(Purple)
                .clickable { onSeleccionarImagen() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = "Seleccionar imagen",
                tint = Color.White,
                modifier = Modifier.size(13.dp)
            )
        }
    }
}

@Composable
private fun UpdateInput(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onTogglePasswordVisibility: (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        textStyle = androidx.compose.ui.text.TextStyle(
            fontSize = 12.sp,
            color = Color.Black
        ),
        placeholder = {
            Text(
                text = placeholder,
                color = Color.Gray,
                fontSize = 12.sp
            )
        },
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = contentDescription,
                tint = Purple,
                modifier = Modifier.size(18.dp)
            )
        },
        trailingIcon = {
            if (isPassword && onTogglePasswordVisibility != null) {
                Icon(
                    imageVector = if (passwordVisible) {
                        Icons.Filled.VisibilityOff
                    } else {
                        Icons.Filled.Visibility
                    },
                    contentDescription = if (passwordVisible) {
                        "Ocultar contraseña"
                    } else {
                        "Mostrar contraseña"
                    },
                    tint = Color.Gray,
                    modifier = Modifier.clickable {
                        onTogglePasswordVisibility()
                    }
                )
            }
        },
        visualTransformation = if (isPassword && !passwordVisible) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = BorderColor,
            unfocusedBorderColor = BorderColor,
            focusedContainerColor = InputBackground,
            unfocusedContainerColor = InputBackground,
            disabledContainerColor = InputBackground,
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            focusedLeadingIconColor = Purple,
            unfocusedLeadingIconColor = Purple,
            focusedTrailingIconColor = Color.Gray,
            unfocusedTrailingIconColor = Color.Gray
        )
    )
}