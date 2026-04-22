package com.example.medication.features.auth.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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

private val MedBlue   = Color(0xFF0077B6)
private val MedPurple = Color(0xFF6650A4)

@Composable
fun RegisterForm(
    usuario: String,
    correo: String,
    contrasena: String,
    repetirContrasena: String,
    selectedRole: String,                  // "caregiver" | "patient"
    isLoading: Boolean,
    errorMessage: String?,
    onUsuarioChange: (String) -> Unit,
    onCorreoChange: (String) -> Unit,
    onContrasenaChange: (String) -> Unit,
    onRepetirContrasenaChange: (String) -> Unit,
    onRoleChange: (String) -> Unit,
    onCrearUsuario: () -> Unit
) {
    var passwordVisible       by remember { mutableStateOf(false) }
    var repeatPasswordVisible by remember { mutableStateOf(false) }

    Card(
        modifier  = Modifier.fillMaxWidth(0.90f).wrapContentHeight(),
        shape     = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors    = CardDefaults.cardColors(containerColor = Color(0xFFF2F2F2))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text       = "Registro",
                fontSize   = 22.sp,
                fontWeight = FontWeight.Bold,
                color      = Color.Black
            )

            Spacer(Modifier.height(20.dp))

            // ── Nombre ─────────────────────────────────────────────────────────
            OutlinedTextField(
                value         = usuario,
                onValueChange = onUsuarioChange,
                modifier      = Modifier.fillMaxWidth(),
                singleLine    = true,
                placeholder   = { Text("Nombre", color = Color.Gray) },
                leadingIcon   = { Icon(Icons.Filled.Person, null, tint = MedPurple) },
                shape  = RoundedCornerShape(8.dp),
                colors = fieldColors()
            )

            Spacer(Modifier.height(12.dp))

            // ── Correo ─────────────────────────────────────────────────────────
            OutlinedTextField(
                value           = correo,
                onValueChange   = onCorreoChange,
                modifier        = Modifier.fillMaxWidth(),
                singleLine      = true,
                placeholder     = { Text("Correo electrónico", color = Color.Gray) },
                leadingIcon     = { Icon(Icons.Filled.Email, null, tint = MedPurple) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                shape  = RoundedCornerShape(8.dp),
                colors = fieldColors()
            )

            Spacer(Modifier.height(12.dp))

            // ── Contraseña ─────────────────────────────────────────────────────
            OutlinedTextField(
                value         = contrasena,
                onValueChange = onContrasenaChange,
                modifier      = Modifier.fillMaxWidth(),
                singleLine    = true,
                placeholder   = { Text("Contraseña", color = Color.Gray) },
                leadingIcon   = { Icon(Icons.Filled.Lock, null, tint = MedPurple) },
                trailingIcon  = {
                    Icon(
                        imageVector     = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                        contentDescription = null,
                        tint     = Color.Gray,
                        modifier = Modifier.clickable { passwordVisible = !passwordVisible }
                    )
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions      = KeyboardOptions(keyboardType = KeyboardType.Password),
                shape  = RoundedCornerShape(8.dp),
                colors = fieldColors()
            )

            Spacer(Modifier.height(12.dp))

            // ── Repetir contraseña ─────────────────────────────────────────────
            OutlinedTextField(
                value         = repetirContrasena,
                onValueChange = onRepetirContrasenaChange,
                modifier      = Modifier.fillMaxWidth(),
                singleLine    = true,
                placeholder   = { Text("Repetir contraseña", color = Color.Gray) },
                leadingIcon   = { Icon(Icons.Filled.Lock, null, tint = MedPurple) },
                trailingIcon  = {
                    Icon(
                        imageVector        = if (repeatPasswordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                        contentDescription = null,
                        tint     = Color.Gray,
                        modifier = Modifier.clickable { repeatPasswordVisible = !repeatPasswordVisible }
                    )
                },
                visualTransformation = if (repeatPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions      = KeyboardOptions(keyboardType = KeyboardType.Password),
                shape  = RoundedCornerShape(8.dp),
                colors = fieldColors()
            )

            Spacer(Modifier.height(16.dp))

            // ── Selector de rol ────────────────────────────────────────────────
            Text(
                text       = "Tipo de cuenta",
                fontSize   = 13.sp,
                fontWeight = FontWeight.Medium,
                color      = Color.DarkGray,
                modifier   = Modifier.align(Alignment.Start)
            )
            Spacer(Modifier.height(6.dp))
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                RoleChip(
                    label     = "Cuidador",
                    selected  = selectedRole == "caregiver",
                    onClick   = { onRoleChange("caregiver") },
                    modifier  = Modifier.weight(1f)
                )
                RoleChip(
                    label    = "Paciente",
                    selected = selectedRole == "patient",
                    onClick  = { onRoleChange("patient") },
                    modifier = Modifier.weight(1f)
                )
            }

            // ── Error ──────────────────────────────────────────────────────────
            if (errorMessage != null) {
                Spacer(Modifier.height(8.dp))
                Text(text = errorMessage, color = Color.Red, fontSize = 12.sp)
            }

            Spacer(Modifier.height(24.dp))

            // ── Botón crear ────────────────────────────────────────────────────
            Button(
                onClick  = onCrearUsuario,
                modifier = Modifier.fillMaxWidth(0.75f).height(46.dp),
                shape    = RoundedCornerShape(8.dp),
                colors   = ButtonDefaults.buttonColors(containerColor = MedBlue),
                enabled  = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color       = Color.White,
                        modifier    = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text       = "Crear Usuario",
                        color      = Color.White,
                        fontSize   = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

// ── Chip de rol ────────────────────────────────────────────────────────────────
@Composable
private fun RoleChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bg     = if (selected) Color(0xFF0077B6) else Color.White
    val border = if (selected) Color(0xFF0077B6) else Color(0xFFCCCCCC)
    val text   = if (selected) Color.White else Color.DarkGray

    OutlinedButton(
        onClick  = onClick,
        modifier = modifier.height(40.dp),
        shape    = RoundedCornerShape(8.dp),
        colors   = ButtonDefaults.outlinedButtonColors(containerColor = bg),
        border   = androidx.compose.foundation.BorderStroke(1.dp, border)
    ) {
        Text(label, color = text, fontSize = 13.sp, fontWeight = FontWeight.Medium)
    }
}

// ── Colores compartidos para campos ───────────────────────────────────────────
@Composable
private fun fieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor      = MedPurple,
    unfocusedBorderColor    = Color(0xFFCCCCCC),
    focusedContainerColor   = Color.White,
    unfocusedContainerColor = Color.White
)