package com.example.medication.features.medication.presentation.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import java.io.File

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
    var name by remember { mutableStateOf("") }
    var dosage by remember { mutableStateOf("") }
    var form by remember { mutableStateOf("") }
    var instructions by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var isActive by remember { mutableStateOf(true) }
    var photoPath by remember { mutableStateOf<String?>(null) }
    var photoUri by remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current
    val scrollState = rememberScrollState()

    fun createImageFile(): File {
        val dir = File(context.filesDir, "medication_photos")
        if (!dir.exists()) dir.mkdirs()
        return File(dir, "med_${System.currentTimeMillis()}.jpg")
    }

    var imageFile by remember { mutableStateOf(createImageFile()) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            photoPath = imageFile.absolutePath
            photoUri = Uri.fromFile(imageFile)
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            imageFile = createImageFile()
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                imageFile
            )
            cameraLauncher.launch(uri)
        }
    }

    Column(
        modifier = Modifier.verticalScroll(scrollState),  // ✅ Scroll agregado aquí
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Registrar Medicamento", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = name, onValueChange = { name = it },
            label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = dosage, onValueChange = { dosage = it },
            label = { Text("Dosis (ej. 500mg)") }, modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = form, onValueChange = { form = it },
            label = { Text("Forma (ej. Cápsula, Jarabe)") }, modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = instructions, onValueChange = { instructions = it },
            label = { Text("Instrucciones") }, modifier = Modifier.fillMaxWidth(), minLines = 2
        )
        OutlinedTextField(
            value = notes, onValueChange = { notes = it },
            label = { Text("Notas") }, modifier = Modifier.fillMaxWidth(), minLines = 2
        )
        OutlinedTextField(
            value = quantity, onValueChange = { quantity = it },
            label = { Text("Cantidad") }, modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = price, onValueChange = { price = it },
            label = { Text("Precio") }, modifier = Modifier.fillMaxWidth()
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Activo", style = MaterialTheme.typography.bodyLarge)
            Switch(checked = isActive, onCheckedChange = { isActive = it })
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (photoUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(photoUri),
                        contentDescription = "Foto del medicamento",
                        modifier = Modifier.fillMaxWidth().height(200.dp).clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
                Button(
                    onClick = { permissionLauncher.launch(android.Manifest.permission.CAMERA) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Camera, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(if (photoUri == null) "📷 Tomar foto" else "📷 Cambiar foto")
                }
            }
        }

        Button(
            onClick = { onRegister(name, dosage, form, instructions, notes, quantity, price, isActive, photoPath) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar Medicamento")
        }
    }
}