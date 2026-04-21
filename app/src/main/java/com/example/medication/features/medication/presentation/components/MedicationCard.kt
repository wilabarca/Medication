package com.example.medication.features.medication.presentation.components

import android.net.Uri
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.medication.features.medication.domain.entities.Medication
import java.io.File

// ── Paleta médica ──────────────────────────────────────────
val MedBlue      = Color(0xFF1A73E8)
val MedBlueSoft  = Color(0xFFD2E3FC)
val MedGreen     = Color(0xFF1E8E3E)
val MedGreenSoft = Color(0xFFCEEAD6)
val MedRed       = Color(0xFFD93025)
val MedRedSoft   = Color(0xFFFCE8E6)
val MedGold      = Color(0xFFFBBC04)
val MedGoldSoft  = Color(0xFFFEF0CD)
val MedSurface   = Color(0xFFF8FBFF)
val MedCard      = Color(0xFFFFFFFF)

@Composable
fun MedicationCard(
    medication: Medication,
    onDelete: (String) -> Unit = {},
    onEdit: (Medication) -> Unit = {},
    isFavorite: Boolean = false,
    onToggleFavorite: () -> Unit = {}
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }

    // Animación de escala al aparecer
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "card_scale"
    )

    // Animación estrella favorito
    val starScale by animateFloatAsState(
        targetValue = if (isFavorite) 1.3f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "star_scale"
    )

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            containerColor = MedCard,
            icon = {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(MedRedSoft, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Rounded.Delete, contentDescription = null, tint = MedRed, modifier = Modifier.size(24.dp))
                }
            },
            title = {
                Text("Eliminar medicamento", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            },
            text = {
                Text("¿Eliminar \"${medication.name}\"? Esta acción no se puede deshacer.", color = Color(0xFF5F6368))
            },
            confirmButton = {
                Button(
                    onClick = { showDeleteDialog = false; onDelete(medication.id) },
                    colors = ButtonDefaults.buttonColors(containerColor = MedRed),
                    shape = RoundedCornerShape(12.dp)
                ) { Text("Eliminar", fontWeight = FontWeight.SemiBold) }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showDeleteDialog = false },
                    shape = RoundedCornerShape(12.dp)
                ) { Text("Cancelar") }
            }
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 6.dp)
            .scale(scale),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MedCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp, hoveredElevation = 8.dp)
    ) {
        Column {
            // ── Franja de color superior ──────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(MedBlue, Color(0xFF4FC3F7), MedGreen)
                        )
                    )
            )

            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {

                // ── Foto ──────────────────────────────────
                medication.photoPath?.let { path ->
                    val file = File(path)
                    if (file.exists()) {
                        Image(
                            painter = rememberAsyncImagePainter(Uri.fromFile(file)),
                            contentDescription = "Foto de ${medication.name}",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                                .clip(RoundedCornerShape(14.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                // ── Header: nombre + acciones ─────────────
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(MedBlueSoft, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Rounded.Medication, contentDescription = null, tint = MedBlue, modifier = Modifier.size(22.dp))
                        }
                        Spacer(Modifier.width(10.dp))
                        Column {
                            Text(medication.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF1A1A2E))
                            Text(medication.form, fontSize = 12.sp, color = MedBlue, fontWeight = FontWeight.Medium)
                        }
                    }

                    Row {
                        // Estrella con animación bounce
                        IconButton(onClick = onToggleFavorite) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.StarOutline,
                                contentDescription = "Favorito",
                                tint = if (isFavorite) MedGold else Color(0xFFBDBDBD),
                                modifier = Modifier
                                    .size(24.dp)
                                    .scale(starScale)
                            )
                        }
                        IconButton(onClick = { onEdit(medication) }) {
                            Icon(Icons.Rounded.Edit, contentDescription = "Editar", tint = MedBlue, modifier = Modifier.size(20.dp))
                        }
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(Icons.Rounded.Delete, contentDescription = "Eliminar", tint = MedRed, modifier = Modifier.size(20.dp))
                        }
                    }
                }

                HorizontalDivider(color = Color(0xFFF0F4FF), thickness = 1.dp)

                // ── Chips de info ─────────────────────────
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    MedChip(icon = Icons.Rounded.Science, text = medication.dosage, color = MedBlue, bg = MedBlueSoft)
                    MedChip(icon = Icons.Rounded.Inventory2, text = "${medication.quantity} uds", color = MedGreen, bg = MedGreenSoft)
                    medication.price?.let {
                        MedChip(icon = Icons.Rounded.AttachMoney, text = "$$it", color = Color(0xFF7B1FA2), bg = Color(0xFFF3E5F5))
                    }
                }

                // ── Estado activo ─────────────────────────
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(
                                if (medication.isActive) MedGreen else Color(0xFFBDBDBD),
                                CircleShape
                            )
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = if (medication.isActive) "Activo" else "Inactivo",
                        fontSize = 12.sp,
                        color = if (medication.isActive) MedGreen else Color(0xFF9E9E9E),
                        fontWeight = FontWeight.Medium
                    )
                }

                // ── Expandible: indicaciones y notas ──────
                val hasExtra = !medication.instructions.isNullOrBlank() || !medication.notes.isNullOrBlank()
                if (hasExtra) {
                    TextButton(
                        onClick = { expanded = !expanded },
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            if (expanded) "Ver menos ▲" else "Ver más ▼",
                            fontSize = 12.sp, color = MedBlue
                        )
                    }

                    AnimatedVisibility(
                        visible = expanded,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            medication.instructions?.takeIf { it.isNotBlank() }?.let {
                                MedInfoRow(icon = Icons.Rounded.Info, label = "Indicaciones", value = it, color = MedBlue)
                            }
                            medication.notes?.takeIf { it.isNotBlank() }?.let {
                                MedInfoRow(icon = Icons.Rounded.Notes, label = "Notas", value = it, color = Color(0xFF7B1FA2))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MedChip(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String, color: Color, bg: Color) {
    Row(
        modifier = Modifier
            .background(bg, RoundedCornerShape(20.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(14.dp))
        Text(text, fontSize = 11.sp, color = color, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun MedInfoRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color.copy(alpha = 0.06f), RoundedCornerShape(10.dp))
            .padding(10.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(16.dp))
        Column {
            Text(label, fontSize = 10.sp, color = color, fontWeight = FontWeight.Bold)
            Text(value, fontSize = 12.sp, color = Color(0xFF424242))
        }
    }
}