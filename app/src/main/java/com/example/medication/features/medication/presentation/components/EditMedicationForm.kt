package com.example.medication.features.medication.presentation.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import com.example.medication.features.medication.domain.entities.Medication
import java.io.File

// ── Paleta clínica – Edición (ámbar quirúrgico) ───────────────────────────────
private val EdPrimary      = Color(0xFF1565C0)   // Azul farmacia
private val EdPrimaryDark  = Color(0xFF0D47A1)
private val EdPrimaryLight = Color(0xFFE3F2FD)
private val EdGreen        = Color(0xFF2E7D32)   // Verde salud
private val EdGreenLight   = Color(0xFFE8F5E9)
private val EdTeal         = Color(0xFF00695C)   // Teal quirúrgico
private val EdRed          = Color(0xFFC62828)   // Rojo urgencias
private val EdAmber        = Color(0xFFE65100)   // Ámbar – acento edición
private val EdAmberLight   = Color(0xFFFFF3E0)
private val EdNeutralBg    = Color(0xFFF0F4F8)
private val EdCardBg       = Color(0xFFFFFFFF)
private val EdTextPrimary  = Color(0xFF1A2027)
private val EdTextSec      = Color(0xFF546E7A)
private val EdDivider      = Color(0xFFECEFF1)

// ── Campo con ícono ────────────────────────────────────────────────────────────
@Composable
private fun MedField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    minLines: Int = 1
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, fontSize = 13.sp) },
        leadingIcon = {
            Icon(icon, contentDescription = null, tint = EdPrimary, modifier = Modifier.size(20.dp))
        },
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = EdPrimary,
            unfocusedBorderColor = Color(0xFFB0BEC5),
            focusedLabelColor = EdPrimary,
            unfocusedContainerColor = EdCardBg,
            focusedContainerColor = EdCardBg
        ),
        modifier = modifier.fillMaxWidth(),
        minLines = minLines
    )
}

// ── Encabezado de sección ──────────────────────────────────────────────────────
@Composable
private fun SectionHeader(title: String, icon: ImageVector) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(32.dp).background(EdPrimaryLight, CircleShape)
        ) {
            Icon(icon, contentDescription = null, tint = EdPrimary, modifier = Modifier.size(18.dp))
        }
        Spacer(Modifier.width(10.dp))
        Text(title, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = EdPrimary, letterSpacing = 0.5.sp)
        Spacer(Modifier.width(10.dp))
        HorizontalDivider(color = EdDivider, modifier = Modifier.weight(1f))
    }
}

/**
 * Diálogo de actualización exitosa.
 * Expuesto como público para que [EditMedicationScreen] lo controle.
 */
@Composable
fun MedicationUpdateSuccessDialog(medicationName: String, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = EdCardBg),
            modifier = Modifier.fillMaxWidth().shadow(16.dp, RoundedCornerShape(24.dp))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(32.dp)
            ) {
                // Ícono con color ámbar para diferenciar de "registrado" (verde)
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(80.dp)
                        .background(
                            Brush.radialGradient(listOf(EdAmberLight, Color(0xFFFFCC80))),
                            CircleShape
                        )
                ) {
                    Icon(
                        Icons.Default.EditNote,
                        contentDescription = null,
                        tint = EdAmber,
                        modifier = Modifier.size(48.dp)
                    )
                }

                Spacer(Modifier.height(20.dp))

                Text(
                    "¡Medicamento Actualizado!",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = EdTextPrimary,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(10.dp))

                // Chip con nombre del medicamento
                Box(
                    modifier = Modifier
                        .background(EdPrimaryLight, RoundedCornerShape(20.dp))
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.MedicalServices,
                            contentDescription = null,
                            tint = EdPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            medicationName,
                            fontWeight = FontWeight.SemiBold,
                            color = EdPrimary,
                            fontSize = 14.sp
                        )
                    }
                }

                Spacer(Modifier.height(10.dp))

                Text(
                    "Los cambios fueron guardados exitosamente en el inventario.",
                    color = EdTextSec,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = EdPrimary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Done, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Aceptar", fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

/**
 * Formulario de edición.
 * Responsabilidad: precargar los datos del medicamento y llamar [onUpdate].
 * NO decide si la actualización fue exitosa — eso lo hace el ViewModel.
 */
@Composable
fun EditMedicationForm(
    medication: Medication,
    onUpdate: (
        name: String,
        dosage: String,
        form: String,
        instructions: String?,
        notes: String?,
        quantity: String,
        price: String,
        isActive: Boolean,
        photoPath: String?
    ) -> Unit
) {
    var name          by rememberSaveable { mutableStateOf(medication.name) }
    var dosage        by rememberSaveable { mutableStateOf(medication.dosage) }
    var form          by rememberSaveable { mutableStateOf(medication.form) }
    var instructions  by rememberSaveable { mutableStateOf(medication.instructions ?: "") }
    var notes         by rememberSaveable { mutableStateOf(medication.notes ?: "") }
    var quantity      by rememberSaveable { mutableStateOf(medication.quantity.toString()) }
    var price         by rememberSaveable { mutableStateOf(medication.price?.toString() ?: "") }
    var isActive      by rememberSaveable { mutableStateOf(medication.isActive) }
    var photoPath     by rememberSaveable { mutableStateOf<String?>(medication.photoPath) }
    var imageFilePath by rememberSaveable { mutableStateOf<String?>(null) }

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        android.util.Log.d("PHOTO_DEBUG", "photoPath recibido: $photoPath")
        android.util.Log.d("PHOTO_DEBUG", "archivo existe: ${photoPath?.let { File(it).exists() }}")
    }

    val photoUri = remember(photoPath) {
        photoPath?.let { path ->
            val file = File(path)
            if (file.exists()) Uri.fromFile(file) else null
        }
    }

    fun createImageFile(): File {
        val dir = File(context.filesDir, "medication_photos")
        if (!dir.exists()) dir.mkdirs()
        return File(dir, "med_${System.currentTimeMillis()}.jpg").also {
            imageFilePath = it.absolutePath
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            imageFilePath?.let { path ->
                photoPath = path
                android.util.Log.d("PHOTO_DEBUG", "Nueva foto guardada en: $path")
            }
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val file = createImageFile()
            val uri = FileProvider.getUriForFile(
                context, "${context.packageName}.fileprovider", file
            )
            cameraLauncher.launch(uri)
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(EdNeutralBg)) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // ── Header con gradiente ámbar → teal (distinto al de registro) ──
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(listOf(EdAmber, EdTeal)),
                        RoundedCornerShape(20.dp)
                    )
                    .padding(20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(52.dp)
                            .background(Color.White.copy(alpha = 0.15f), CircleShape)
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Spacer(Modifier.width(14.dp))
                    Column {
                        Text(
                            "Editar Medicamento",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Text(
                            medication.name,
                            color = Color.White.copy(alpha = 0.80f),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // ── Información básica ─────────────────────────────────────────
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = EdCardBg),
                modifier = Modifier.fillMaxWidth().shadow(2.dp, RoundedCornerShape(16.dp))
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.padding(16.dp)) {
                    SectionHeader("Información Básica", Icons.Default.Info)
                    MedField(name, { name = it }, "Nombre del medicamento", Icons.Default.LocalPharmacy)
                    MedField(dosage, { dosage = it }, "Dosis (ej. 500mg)", Icons.Default.Scale)
                    MedField(form, { form = it }, "Forma (ej. Cápsula, Jarabe)", Icons.Outlined.Medication)
                }
            }

            // ── Indicaciones ───────────────────────────────────────────────
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = EdCardBg),
                modifier = Modifier.fillMaxWidth().shadow(2.dp, RoundedCornerShape(16.dp))
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.padding(16.dp)) {
                    SectionHeader("Indicaciones", Icons.Default.Assignment)
                    MedField(instructions, { instructions = it }, "Instrucciones de uso", Icons.Default.ListAlt, minLines = 2)
                    MedField(notes, { notes = it }, "Notas adicionales", Icons.Default.StickyNote2, minLines = 2)
                }
            }

            // ── Inventario ─────────────────────────────────────────────────
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = EdCardBg),
                modifier = Modifier.fillMaxWidth().shadow(2.dp, RoundedCornerShape(16.dp))
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.padding(16.dp)) {
                    SectionHeader("Inventario y Precio", Icons.Default.Inventory)
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                        MedField(quantity, { quantity = it }, "Cantidad", Icons.Default.Numbers, modifier = Modifier.weight(1f))
                        MedField(price, { price = it }, "Precio ($)", Icons.Default.AttachMoney, modifier = Modifier.weight(1f))
                    }
                    // Toggle activo/inactivo
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                if (isActive) EdGreenLight else Color(0xFFFFEBEE),
                                RoundedCornerShape(12.dp)
                            )
                            .border(
                                1.dp,
                                if (isActive) Color(0xFFA5D6A7) else Color(0xFFEF9A9A),
                                RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 14.dp, vertical = 4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    if (isActive) Icons.Default.CheckCircle else Icons.Default.Cancel,
                                    contentDescription = null,
                                    tint = if (isActive) EdGreen else EdRed,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(Modifier.width(8.dp))
                                Column {
                                    Text("Estado del medicamento", fontWeight = FontWeight.Medium, fontSize = 13.sp, color = EdTextPrimary)
                                    Text(
                                        if (isActive) "Activo · Disponible en inventario" else "Inactivo · No disponible",
                                        fontSize = 11.sp, color = EdTextSec
                                    )
                                }
                            }
                            Switch(
                                checked = isActive,
                                onCheckedChange = { isActive = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = EdGreen,
                                    checkedTrackColor = Color(0xFFA5D6A7),
                                    uncheckedThumbColor = EdRed,
                                    uncheckedTrackColor = Color(0xFFEF9A9A)
                                )
                            )
                        }
                    }
                }
            }

            // ── Fotografía ─────────────────────────────────────────────────
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = EdCardBg),
                modifier = Modifier.fillMaxWidth().shadow(2.dp, RoundedCornerShape(16.dp))
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.padding(16.dp)) {
                    SectionHeader("Fotografía", Icons.Default.CameraAlt)
                    if (photoUri != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .border(2.dp, EdPrimaryLight, RoundedCornerShape(12.dp))
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(photoUri),
                                contentDescription = "Foto del medicamento",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(8.dp)
                                    .background(EdGreen, CircleShape)
                                    .padding(4.dp)
                            ) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                            }
                        }
                    } else {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp)
                                .background(Color(0xFFF0F4F8), RoundedCornerShape(12.dp))
                                .border(1.dp, Color(0xFFCFD8DC), RoundedCornerShape(12.dp))
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.ImageNotSupported, contentDescription = null, tint = EdTextSec, modifier = Modifier.size(20.dp))
                                Spacer(Modifier.width(8.dp))
                                Text("Sin fotografía", color = EdTextSec, fontSize = 13.sp)
                            }
                        }
                    }
                    OutlinedButton(
                        onClick = { permissionLauncher.launch(android.Manifest.permission.CAMERA) },
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, EdPrimary),
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = EdPrimary)
                    ) {
                        Icon(Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(
                            if (photoUri == null) "Agregar foto" else "Cambiar fotografía",
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // ── Botón actualizar ───────────────────────────────────────────
            Button(
                onClick = {
                    onUpdate(
                        name,
                        dosage,
                        form,
                        instructions.ifBlank { null },
                        notes.ifBlank { null },
                        quantity,
                        price,
                        isActive,
                        photoPath
                    )
                },
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = EdAmber),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .shadow(6.dp, RoundedCornerShape(14.dp))
            ) {
                Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(10.dp))
                Text("Actualizar Medicamento", fontWeight = FontWeight.Bold, fontSize = 16.sp, letterSpacing = 0.3.sp)
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}