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
import java.io.File

// ── Paleta médica ──────────────────────────────────────────────────────────────
private val MedBlue       = Color(0xFF1565C0)
private val MedBlueDark   = Color(0xFF0D47A1)
private val MedBlueLight  = Color(0xFFE3F2FD)
private val MedGreen      = Color(0xFF2E7D32)
private val MedGreenLight = Color(0xFFE8F5E9)
private val MedTeal       = Color(0xFF00695C)
private val MedRed        = Color(0xFFC62828)
private val NeutralBg     = Color(0xFFF5F7FA)
private val CardBg        = Color(0xFFFFFFFF)
private val TextPrimary   = Color(0xFF1A2027)
private val TextSecondary = Color(0xFF546E7A)
private val Divider       = Color(0xFFECEFF1)

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
            Icon(icon, contentDescription = null, tint = MedBlue, modifier = Modifier.size(20.dp))
        },
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MedBlue,
            unfocusedBorderColor = Color(0xFFB0BEC5),
            focusedLabelColor = MedBlue,
            unfocusedContainerColor = CardBg,
            focusedContainerColor = CardBg
        ),
        modifier = modifier.fillMaxWidth(),
        minLines = minLines
    )
}

@Composable
private fun SectionHeader(title: String, icon: ImageVector) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(32.dp).background(MedBlueLight, CircleShape)
        ) {
            Icon(icon, contentDescription = null, tint = MedBlue, modifier = Modifier.size(18.dp))
        }
        Spacer(Modifier.width(10.dp))
        Text(title, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = MedBlue, letterSpacing = 0.5.sp)
        Spacer(Modifier.width(10.dp))
        HorizontalDivider(color = Divider, modifier = Modifier.weight(1f))
    }
}

/**
 * Diálogo de éxito — expuesto como composable público para que
 * [RegisterMedicationScreen] lo muestre cuando el ViewModel confirma el éxito.
 */
@Composable
fun MedicationSuccessDialog(medicationName: String, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = CardBg),
            modifier = Modifier.fillMaxWidth().shadow(16.dp, RoundedCornerShape(24.dp))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(32.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(80.dp)
                        .background(
                            Brush.radialGradient(listOf(MedGreenLight, Color(0xFFA5D6A7))),
                            CircleShape
                        )
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = MedGreen,
                        modifier = Modifier.size(48.dp)
                    )
                }
                Spacer(Modifier.height(20.dp))
                Text(
                    "¡Medicamento Registrado!",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = TextPrimary,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .background(MedBlueLight, RoundedCornerShape(20.dp))
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.MedicalServices, contentDescription = null, tint = MedBlue, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(6.dp))
                        Text(medicationName, fontWeight = FontWeight.SemiBold, color = MedBlue, fontSize = 14.sp)
                    }
                }
                Spacer(Modifier.height(10.dp))
                Text(
                    "El medicamento fue agregado exitosamente al inventario.",
                    color = TextSecondary,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(24.dp))
                Button(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MedBlue),
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
 * Formulario de registro.
 * Responsabilidad: capturar datos y llamar [onRegister].
 * NO decide si el registro fue exitoso — eso lo hace el ViewModel.
 */
@Composable
fun RegisterMedicationForm(
    onRegister: (
        name: String,
        dosage: String,
        form: String,
        instructions: String,
        notes: String,
        quantity: String,
        price: String,
        isActive: Boolean,
        photoPath: String?
    ) -> Unit
) {
    var name         by remember { mutableStateOf("") }
    var dosage       by remember { mutableStateOf("") }
    var form         by remember { mutableStateOf("") }
    var instructions by remember { mutableStateOf("") }
    var notes        by remember { mutableStateOf("") }
    var quantity     by remember { mutableStateOf("") }
    var price        by remember { mutableStateOf("") }
    var isActive     by remember { mutableStateOf(true) }
    var photoPath    by remember { mutableStateOf<String?>(null) }
    var photoUri     by remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current

    fun createImageFile(): File {
        val dir = File(context.filesDir, "medication_photos")
        if (!dir.exists()) dir.mkdirs()
        return File(dir, "med_${System.currentTimeMillis()}.jpg")
    }

    var imageFile by remember { mutableStateOf(createImageFile()) }

    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            photoPath = imageFile.absolutePath
            photoUri  = Uri.fromFile(imageFile)
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            imageFile = createImageFile()
            val uri = FileProvider.getUriForFile(
                context, "${context.packageName}.fileprovider", imageFile
            )
            cameraLauncher.launch(uri)
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(NeutralBg)) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // ── Header ─────────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(listOf(MedBlueDark, MedTeal)),
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
                        Icon(Icons.Default.MedicalServices, contentDescription = null, tint = Color.White, modifier = Modifier.size(28.dp))
                    }
                    Spacer(Modifier.width(14.dp))
                    Column {
                        Text("Registrar Medicamento", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Text("Ingresa los datos del medicamento", color = Color.White.copy(alpha = 0.75f), fontSize = 12.sp)
                    }
                }
            }

            // ── Información básica ─────────────────────────────────────────
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardBg),
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
                colors = CardDefaults.cardColors(containerColor = CardBg),
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
                colors = CardDefaults.cardColors(containerColor = CardBg),
                modifier = Modifier.fillMaxWidth().shadow(2.dp, RoundedCornerShape(16.dp))
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.padding(16.dp)) {
                    SectionHeader("Inventario y Precio", Icons.Default.Inventory)
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                        MedField(quantity, { quantity = it }, "Cantidad", Icons.Default.Numbers, modifier = Modifier.weight(1f))
                        MedField(price, { price = it }, "Precio ($)", Icons.Default.AttachMoney, modifier = Modifier.weight(1f))
                    }
                    // Toggle activo/inactivo con estilo médico
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(if (isActive) MedGreenLight else Color(0xFFFFEBEE), RoundedCornerShape(12.dp))
                            .border(1.dp, if (isActive) Color(0xFFA5D6A7) else Color(0xFFEF9A9A), RoundedCornerShape(12.dp))
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
                                    tint = if (isActive) MedGreen else MedRed,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(Modifier.width(8.dp))
                                Column {
                                    Text("Estado del medicamento", fontWeight = FontWeight.Medium, fontSize = 13.sp, color = TextPrimary)
                                    Text(
                                        if (isActive) "Activo · Disponible en inventario" else "Inactivo · No disponible",
                                        fontSize = 11.sp, color = TextSecondary
                                    )
                                }
                            }
                            Switch(
                                checked = isActive,
                                onCheckedChange = { isActive = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = MedGreen,
                                    checkedTrackColor = Color(0xFFA5D6A7),
                                    uncheckedThumbColor = MedRed,
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
                colors = CardDefaults.cardColors(containerColor = CardBg),
                modifier = Modifier.fillMaxWidth().shadow(2.dp, RoundedCornerShape(16.dp))
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.padding(16.dp)) {
                    SectionHeader("Fotografía", Icons.Default.CameraAlt)
                    AnimatedVisibility(visible = photoUri != null, enter = fadeIn() + expandVertically()) {
                        if (photoUri != null) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(2.dp, MedBlueLight, RoundedCornerShape(12.dp))
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
                                        .background(MedGreen, CircleShape)
                                        .padding(4.dp)
                                ) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                                }
                            }
                        }
                    }
                    OutlinedButton(
                        onClick = { permissionLauncher.launch(android.Manifest.permission.CAMERA) },
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, MedBlue),
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MedBlue)
                    ) {
                        Icon(Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(
                            if (photoUri == null) "Tomar foto del medicamento" else "Cambiar fotografía",
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // ── Botón guardar — solo dispara onRegister, sin lógica de éxito ──
            Button(
                onClick = {
                    onRegister(name, dosage, form, instructions, notes, quantity, price, isActive, photoPath)
                },
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MedBlue),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .shadow(6.dp, RoundedCornerShape(14.dp))
            ) {
                Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(10.dp))
                Text("Guardar Medicamento", fontWeight = FontWeight.Bold, fontSize = 16.sp, letterSpacing = 0.3.sp)
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}