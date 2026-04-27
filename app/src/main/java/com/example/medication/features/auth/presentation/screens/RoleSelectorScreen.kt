package com.example.medication.features.auth.presentation.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ── Paleta selector ────────────────────────────────────────────────────────────
private val RsTeal      = Color(0xFF00695C)
private val RsTealLight = Color(0xFFE0F2F1)
private val RsTealDark  = Color(0xFF004D40)
private val RsBlue      = Color(0xFF1565C0)
private val RsBlueLight = Color(0xFFE3F2FD)
private val RsBlueDark  = Color(0xFF0D47A1)
private val RsPurple    = Color(0xFF6A1B9A)
private val RsText      = Color(0xFF0D1F2D)
private val RsTextSec   = Color(0xFF546E7A)

@Composable
fun RoleSelectorScreen(
    onCaregiverSelected: () -> Unit,
    onPatientSelected: () -> Unit
) {
    var selectedRole by remember { mutableStateOf<String?>(null) }
    var visible      by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { visible = true }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF003F5C),
                        Color(0xFF0D47A1),
                        Color(0xFF00695C)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = visible,
            enter   = fadeIn(tween(600)) + slideInVertically(
                animationSpec  = tween(600, easing = FastOutSlowInEasing),
                initialOffsetY = { it / 4 }
            )
        ) {
            Column(
                modifier            = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // ── Logo / Título ──────────────────────────────────────────────
                Box(
                    modifier         = Modifier
                        .size(90.dp)
                        .background(Color.White.copy(alpha = 0.15f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Rounded.MedicalServices,
                        contentDescription = null,
                        tint     = Color.White,
                        modifier = Modifier.size(50.dp)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "MedControl",
                        fontWeight = FontWeight.Bold,
                        fontSize   = 30.sp,
                        color      = Color.White
                    )
                    Text(
                        "¿Cómo quieres ingresar?",
                        fontSize  = 15.sp,
                        color     = Color.White.copy(alpha = 0.80f),
                        textAlign = TextAlign.Center
                    )
                }

                // ── Cards de rol ───────────────────────────────────────────────
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    RoleCard(
                        title       = "Cuidador",
                        subtitle    = "Administra pacientes y medicamentos",
                        icon        = Icons.Rounded.ManageAccounts,
                        gradient    = listOf(RsTealDark, RsTeal),
                        borderColor = RsTeal,
                        selected    = selectedRole == "caregiver",
                        onClick     = { selectedRole = "caregiver" },
                        modifier    = Modifier.weight(1f)
                    )
                    RoleCard(
                        title       = "Paciente",
                        subtitle    = "Ve tus medicamentos y vincúlate",
                        icon        = Icons.Rounded.Person,
                        gradient    = listOf(RsBlueDark, RsBlue),
                        borderColor = RsBlue,
                        selected    = selectedRole == "patient",
                        onClick     = { selectedRole = "patient" },
                        modifier    = Modifier.weight(1f)
                    )
                }

                // ── Descripción del rol seleccionado ───────────────────────────
                AnimatedVisibility(
                    visible = selectedRole != null,
                    enter   = fadeIn(tween(300)) + expandVertically(tween(300)),
                    exit    = fadeOut(tween(200)) + shrinkVertically(tween(200))
                ) {
                    Card(
                        shape  = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White.copy(alpha = 0.12f)
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                imageVector = if (selectedRole == "caregiver")
                                    Icons.Rounded.Info else Icons.Rounded.Link,
                                contentDescription = null,
                                tint     = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = if (selectedRole == "caregiver")
                                    "Como cuidador podrás registrar pacientes, asignarles medicamentos y generar códigos de vinculación."
                                else
                                    "Como paciente verás tus medicamentos. Si un cuidador te asignó uno, vincúlate con su código.",
                                fontSize = 12.sp,
                                color    = Color.White.copy(alpha = 0.90f),
                                lineHeight = 18.sp
                            )
                        }
                    }
                }

                // ── Botón continuar ────────────────────────────────────────────
                AnimatedVisibility(
                    visible = selectedRole != null,
                    enter   = fadeIn(tween(300)) + scaleIn(tween(300)),
                    exit    = fadeOut(tween(200)) + scaleOut(tween(200))
                ) {
                    Button(
                        onClick = {
                            when (selectedRole) {
                                "caregiver" -> onCaregiverSelected()
                                "patient"   -> onPatientSelected()
                            }
                        },
                        shape    = RoundedCornerShape(14.dp),
                        colors   = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor   = RsText
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .shadow(8.dp, RoundedCornerShape(14.dp))
                    ) {
                        Icon(
                            Icons.Rounded.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(10.dp))
                        Text(
                            "Continuar como ${
                                if (selectedRole == "caregiver") "Cuidador" else "Paciente"
                            }",
                            fontWeight = FontWeight.Bold,
                            fontSize   = 15.sp
                        )
                    }
                }

                Text(
                    "Puedes cambiar tu rol en cualquier momento",
                    fontSize  = 11.sp,
                    color     = Color.White.copy(alpha = 0.50f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

// ── Card de rol ────────────────────────────────────────────────────────────────
@Composable
private fun RoleCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    gradient: List<Color>,
    borderColor: Color,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(
        targetValue   = if (selected) 1.05f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label         = "role_card_scale"
    )

    Card(
        modifier = modifier
            .scale(scale)
            .clickable { onClick() }
            .then(
                if (selected) Modifier.border(2.dp, borderColor, RoundedCornerShape(18.dp))
                else Modifier.border(1.dp, Color.White.copy(alpha = 0.20f), RoundedCornerShape(18.dp))
            ),
        shape  = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) Color.White else Color.White.copy(alpha = 0.10f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (selected) 8.dp else 2.dp
        )
    ) {
        Column(
            modifier            = Modifier.padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Ícono con gradiente
            Box(
                modifier = Modifier
                    .size(58.dp)
                    .background(
                        if (selected)
                            Brush.linearGradient(gradient)
                        else
                            Brush.linearGradient(
                                listOf(
                                    Color.White.copy(alpha = 0.20f),
                                    Color.White.copy(alpha = 0.10f)
                                )
                            ),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint     = if (selected) Color.White else Color.White.copy(alpha = 0.80f),
                    modifier = Modifier.size(30.dp)
                )
            }

            Text(
                title,
                fontWeight = FontWeight.Bold,
                fontSize   = 14.sp,
                color      = if (selected) gradient.last() else Color.White,
                textAlign  = TextAlign.Center
            )

            Text(
                subtitle,
                fontSize   = 10.sp,
                color      = if (selected) RsTextSec else Color.White.copy(alpha = 0.65f),
                textAlign  = TextAlign.Center,
                lineHeight = 14.sp
            )

            // Check si está seleccionado
            AnimatedVisibility(visible = selected) {
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .background(gradient.last(), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Rounded.Check,
                        contentDescription = null,
                        tint     = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}