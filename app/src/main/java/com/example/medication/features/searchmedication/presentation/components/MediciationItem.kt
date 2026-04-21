package com.example.medication.features.searchmedication.presentation.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.medication.features.searchmedication.domain.entities.Medication

// ── Paleta ítem médico (prefijo Si = Search Item) ─────────────────────────────
private val SiBlue        = Color(0xFF0277BD)   // Azul hospital
private val SiBlueLight   = Color(0xFFE1F5FE)
private val SiBlueGrad1   = Color(0xFF0277BD)
private val SiBlueGrad2   = Color(0xFF26C6DA)   // Cian clínico
private val SiGreen       = Color(0xFF2E7D32)   // Verde salud
private val SiGreenLight  = Color(0xFFE8F5E9)
private val SiPurple      = Color(0xFF6A1B9A)   // Violeta farmacéutico
private val SiPurpleLight = Color(0xFFF3E5F5)
private val SiTeal        = Color(0xFF00695C)
private val SiTealLight   = Color(0xFFE0F2F1)
private val SiText        = Color(0xFF0D1F2D)
private val SiTextSec     = Color(0xFF546E7A)
private val SiCardBg      = Color(0xFFFFFFFF)
private val SiDivider     = Color(0xFFECEFF1)

@Composable
fun MedicineItem(
    medication: Medication,
    onClick: (Medication) -> Unit,
    modifier: Modifier = Modifier,
    animationDelay: Int = 0          // ms de retraso para animación en lista
) {
    // ── Animación de entrada (slide + fade desde abajo) ────────────────────────
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(animationDelay.toLong())
        visible = true
    }

    // ── Escala al presionar ────────────────────────────────────────────────────
    var pressed by remember { mutableStateOf(false) }
    val pressScale by animateFloatAsState(
        targetValue    = if (pressed) 0.96f else 1f,
        animationSpec  = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label          = "press_scale"
    )

    // ── Pulso en el ícono del medicamento ──────────────────────────────────────
    val infiniteTransition = rememberInfiniteTransition(label = "icon_pulse")
    val iconGlow by infiniteTransition.animateFloat(
        initialValue  = 0.85f,
        targetValue   = 1f,
        animationSpec = infiniteRepeatable(
            animation  = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "icon_glow"
    )

    AnimatedVisibility(
        visible = visible,
        enter   = fadeIn(tween(350)) + slideInVertically(
            animationSpec    = tween(350, easing = FastOutSlowInEasing),
            initialOffsetY   = { it / 3 }
        )
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 5.dp)
                .scale(pressScale)
                .clickable {
                    pressed = true
                    onClick(medication)
                },
            shape     = RoundedCornerShape(18.dp),
            colors    = CardDefaults.cardColors(containerColor = SiCardBg),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment    = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // ── Ícono con gradiente médico + pulso ─────────────────────────
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .graphicsLayer {
                            scaleX = iconGlow
                            scaleY = iconGlow
                        }
                        .background(
                            Brush.linearGradient(listOf(SiBlueGrad1, SiBlueGrad2)),
                            RoundedCornerShape(15.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Rounded.Medication,
                        contentDescription = null,
                        tint     = Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                }

                // ── Contenido ──────────────────────────────────────────────────
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        medication.name,
                        fontWeight = FontWeight.Bold,
                        fontSize   = 15.sp,
                        color      = SiText
                    )
                    Text(
                        medication.description,
                        fontSize  = 12.sp,
                        color     = SiTextSec,
                        maxLines  = 1
                    )
                    Spacer(Modifier.height(2.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        MedChip(
                            text = "${medication.quantity} uds",
                            textColor = SiGreen,
                            bgColor   = SiGreenLight,
                            icon      = Icons.Rounded.Inventory2
                        )
                        MedChip(
                            text = "$${medication.price}",
                            textColor = SiPurple,
                            bgColor   = SiPurpleLight,
                            icon      = Icons.Rounded.AttachMoney
                        )
                    }
                }

                // ── Flecha ─────────────────────────────────────────────────────
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(SiBlueLight, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Rounded.ChevronRight,
                        contentDescription = null,
                        tint     = SiBlue,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

// ── Chip con ícono pequeño ─────────────────────────────────────────────────────
@Composable
private fun MedChip(
    text: String,
    textColor: Color,
    bgColor: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Row(
        modifier = Modifier
            .background(bgColor, RoundedCornerShape(20.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp),
        verticalAlignment    = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Icon(icon, contentDescription = null, tint = textColor, modifier = Modifier.size(11.dp))
        Text(
            text       = text,
            fontSize   = 10.sp,
            color      = textColor,
            fontWeight = FontWeight.SemiBold
        )
    }
}