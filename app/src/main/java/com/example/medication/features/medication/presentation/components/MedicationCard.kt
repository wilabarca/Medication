package com.example.medication.features.medication.presentation.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.medication.features.medication.domain.entities.Medication
import java.io.File

@Composable
fun MedicationCard(
    medication: Medication,
    onDelete: (String) -> Unit = {},
    onEdit: (Medication) -> Unit = {},
    isFavorite: Boolean = false,
    onToggleFavorite: () -> Unit = {}
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    // Diálogo de confirmación
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = Color.Red
                )
            },
            title = {
                Text("Eliminar medicamento")
            },
            text = {
                Text("¿Estás seguro de que deseas eliminar \"${medication.name}\"? Esta acción no se puede deshacer.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false
                        onDelete(medication.id)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    )
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            medication.photoPath?.let { path ->
                val file = File(path)
                if (file.exists()) {
                    Image(
                        painter = rememberAsyncImagePainter(Uri.fromFile(file)),
                        contentDescription = "Foto de ${medication.name}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = medication.name,
                    style = MaterialTheme.typography.titleMedium
                )

                Row {
                    IconButton(onClick = onToggleFavorite) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.StarOutline,
                            contentDescription = "Favorito",
                            tint = if (isFavorite) Color(0xFFFFC107) else Color.Gray
                        )
                    }

                    IconButton(onClick = { onEdit(medication) }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    // ← Ahora abre el diálogo en lugar de eliminar directo
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Eliminar",
                            tint = Color.Red
                        )
                    }
                }
            }

            Text(text = "Dosis: ${medication.dosage}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Presentación: ${medication.form}", style = MaterialTheme.typography.bodyMedium)

            medication.instructions?.takeIf { it.isNotBlank() }?.let {
                Text(text = "Indicaciones: $it", style = MaterialTheme.typography.bodySmall)
            }

            medication.notes?.takeIf { it.isNotBlank() }?.let {
                Text(text = "Notas: $it", style = MaterialTheme.typography.bodySmall)
            }

            Text(text = "Cantidad: ${medication.quantity}")
            Text(text = "Precio: ${medication.price ?: 0.0}")
            Text(text = if (medication.isActive) "Activo" else "Inactivo")
        }
    }
}