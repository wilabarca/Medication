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

private val RgPrimary      = Color(0xFF0277BD)
private val RgPrimaryDark  = Color(0xFF01579B)
private val RgPrimaryLight = Color(0xFFE1F5FE)
private val RgGreen        = Color(0xFF388E3C)
private val RgGreenLight   = Color(0xFFF1F8E9)
private val RgTeal         = Color(0xFF00838F)
private val RgRed          = Color(0xFFD32F2F)
private val RgNeutralBg    = Color(0xFFF4F6F9)
private val RgCardBg       = Color(0xFFFFFFFF)
private val RgTextPrimary  = Color(0xFF1C2B3A)
private val RgTextSec      = Color(0xFF607D8B)
private val RgDivider      = Color(0xFFE8EDF2)

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
            Icon(icon, contentDescription = null, tint = RgPrimary, modifier = Modifier.size(20.dp))
        },
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor      = RgPrimary,
            unfocusedBorderColor    = Color(0xFFB0BEC5),
            focusedLabelColor       = RgPrimary,
            unfocusedContainerColor = RgCardBg,
            focusedContainerColor   = RgCardBg
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
            modifier = Modifier.size(32.dp).background(RgPrimaryLight, CircleShape)
        ) {
            Icon(icon, contentDescription = null, tint = RgPrimary, modifier = Modifier.size(18.dp))
        }
        Spacer(Modifier.width(10.dp))
        Text(title, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = RgPrimary, letterSpacing = 0.5.sp)
        Spacer(Modifier.width(10.dp))
        HorizontalDivider(color = RgDivider, modifier = Modifier.weight(1f))
    }
}

@Composable
fun MedicationSuccessDialog(medicationName: String, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = RgCardBg),
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
                            Brush.radialGradient(listOf(RgGreenLight, Color(0xFFA5D6A7))),
                            CircleShape
                        )
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = RgGreen, modifier = Modifier.size(48.dp))
                }
                Spacer(Modifier.height(20.dp))
                Text("¡Medicamento Registrado!", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = RgTextPrimary, textAlign = TextAlign.Center)
                Spacer(Modifier.height(10.dp))
                Box(modifier = Modifier.background(RgPrimaryLight, RoundedCornerShape(20.dp)).padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.MedicalServices, contentDescription = null, tint = RgPrimary, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(6.dp))
                        Text(medicationName, fontWeight = FontWeight.SemiBold, color = RgPrimary, fontSize = 14.sp)
                    }
                }
                Spacer(Modifier.height(10.dp))
                Text("El medicamento fue agregado exitosamente.", color = RgTextSec, fontSize = 13.sp, textAlign = TextAlign.Center)
                Spacer(Modifier.height(24.dp))
                Button(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = RgPrimary),
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
        photoPath: String?,
        startDate: String?,
        endDate: String?
    ) -> Unit
) {
    // ── Estado del formulario — mismo orden que la entidad ────────
    var name         by remember { mutableStateOf("") }  // name
    var dosage       by remember { mutableStateOf("") }  // dosage
    var form         by remember { mutableStateOf("") }  // form
    var instructions by remember { mutableStateOf("") }  // instructions
    var notes        by remember { mutableStateOf("") }  // notes
    var quantity     by remember { mutableStateOf("") }  // quantity
    var price        by remember { mutableStateOf("") }  // price
    var isActive     by remember { mutableStateOf(true) }// isActive
    var startDate    by remember { mutableStateOf("") }  // startDate
    var endDate      by remember { mutableStateOf("") }  // endDate
    var photoPath    by remember { mutableStateOf<String?>(null) }
    var photoUri     by remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current

    fun createImageFile(): File {
        val dir = File(context.filesDir, "medication_photos")
        if (!dir.exists()) dir.mkdirs()
        return File(dir, "med_${System.currentTimeMillis()}.jpg")
    }

    var imageFile by remember { mutableStateOf(createImageFile()) }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            photoPath = imageFile.absolutePath
            photoUri  = Uri.fromFile(imageFile)
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) {
            imageFile = createImageFile()
            val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", imageFile)
            cameraLauncher.launch(uri)
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(RgNeutralBg)) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // ── Header ─────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.horizontalGradient(listOf(RgPrimaryDark, RgTeal)), RoundedCornerShape(20.dp))
                    .padding(20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(52.dp).background(Color.White.copy(alpha = 0.15f), CircleShape)
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

            // ── 1. name / dosage / form ────────────────────────────
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = RgCardBg),
                modifier = Modifier.fillMaxWidth().shadow(2.dp, RoundedCornerShape(16.dp))
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.padding(16.dp)) {
                    SectionHeader("Información básica", Icons.Default.Info)
                    MedField(name,   { name = it },   "Nombre del medicamento",      Icons.Default.LocalPharmacy)
                    MedField(dosage, { dosage = it }, "Dosis (ej. 500mg)",           Icons.Default.Scale)
                    MedField(form,   { form = it },   "Forma (ej. Cápsula, Jarabe)", Icons.Outlined.Medication)
                }
            }

            // ── 2. instructions / notes ────────────────────────────
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = RgCardBg),
                modifier = Modifier.fillMaxWidth().shadow(2.dp, RoundedCornerShape(16.dp))
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.padding(16.dp)) {
                    SectionHeader("Indicaciones", Icons.Default.Assignment)
                    MedField(instructions, { instructions = it }, "Instrucciones de uso",  Icons.Default.ListAlt,    minLines = 2)
                    MedField(notes,        { notes = it },        "Notas adicionales",     Icons.Default.StickyNote2, minLines = 2)
                }
            }

            // ── 3. quantity / price / isActive ─────────────────────
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = RgCardBg),
                modifier = Modifier.fillMaxWidth().shadow(2.dp, RoundedCornerShape(16.dp))
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.padding(16.dp)) {
                    SectionHeader("Inventario y precio", Icons.Default.Inventory)
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                        MedField(quantity, { quantity = it }, "Cantidad",   Icons.Default.Numbers,     modifier = Modifier.weight(1f))
                        MedField(price,    { price = it },    "Precio ($)", Icons.Default.AttachMoney, modifier = Modifier.weight(1f))
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(if (isActive) RgGreenLight else Color(0xFFFFEBEE), RoundedCornerShape(12.dp))
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
                                    tint = if (isActive) RgGreen else RgRed,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(Modifier.width(8.dp))
                                Column {
                                    Text("Estado del medicamento", fontWeight = FontWeight.Medium, fontSize = 13.sp, color = RgTextPrimary)
                                    Text(
                                        if (isActive) "Activo · Disponible" else "Inactivo · No disponible",
                                        fontSize = 11.sp, color = RgTextSec
                                    )
                                }
                            }
                            Switch(
                                checked = isActive,
                                onCheckedChange = { isActive = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor   = RgGreen,
                                    checkedTrackColor   = Color(0xFFA5D6A7),
                                    uncheckedThumbColor = RgRed,
                                    uncheckedTrackColor = Color(0xFFEF9A9A)
                                )
                            )
                        }
                    }
                }
            }

            // ── 4. startDate / endDate ─────────────────────────────
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = RgCardBg),
                modifier = Modifier.fillMaxWidth().shadow(2.dp, RoundedCornerShape(16.dp))
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.padding(16.dp)) {
                    SectionHeader("Duración del tratamiento", Icons.Default.DateRange)
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                        MedField(
                            value         = startDate,
                            onValueChange = { startDate = it },
                            label         = "Inicio (YYYY-MM-DD)",
                            icon          = Icons.Default.CalendarToday,
                            modifier      = Modifier.weight(1f)
                        )
                        MedField(
                            value         = endDate,
                            onValueChange = { endDate = it },
                            label         = "Término (YYYY-MM-DD)",
                            icon          = Icons.Default.EventBusy,
                            modifier      = Modifier.weight(1f)
                        )
                    }
                }
            }

            // ── 5. Fotografía ──────────────────────────────────────
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = RgCardBg),
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
                                    .border(2.dp, RgPrimaryLight, RoundedCornerShape(12.dp))
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
                                        .background(RgGreen, CircleShape)
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
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, RgPrimary),
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = RgPrimary)
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

            // ── Botón guardar ──────────────────────────────────────
            Button(
                onClick = {
                    onRegister(
                        name, dosage, form,
                        instructions, notes,
                        quantity, price,
                        isActive, photoPath,
                        startDate.ifBlank { null },
                        endDate.ifBlank { null }
                    )
                },
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = RgPrimary),
                modifier = Modifier.fillMaxWidth().height(54.dp).shadow(6.dp, RoundedCornerShape(14.dp))
            ) {
                Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(10.dp))
                Text("Guardar Medicamento", fontWeight = FontWeight.Bold, fontSize = 16.sp, letterSpacing = 0.3.sp)
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}