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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import com.example.medication.features.medication.domain.entities.Medication
import java.io.File

@Composable
fun EditMedicationForm(
    medication: Medication,
    onUpdate: (String, String, String, String, String?) -> Unit
) {
    // ← rememberSaveable sobrevive cuando la cámara pausa la actividad
    var name by rememberSaveable { mutableStateOf(medication.name) }
    var quantity by rememberSaveable { mutableStateOf(medication.quantity.toString()) }
    var price by rememberSaveable { mutableStateOf(medication.price.toString()) }
    var description by rememberSaveable { mutableStateOf(medication.description) }
    var photoPath by rememberSaveable { mutableStateOf<String?>(medication.photoPath) }
    var imageFilePath by rememberSaveable { mutableStateOf<String?>(null) }

    val context = LocalContext.current

    // ← Log para verificar que el photoPath llega correctamente desde Room
    LaunchedEffect(Unit) {
        android.util.Log.d("PHOTO_DEBUG", "photoPath recibido: $photoPath")
        android.util.Log.d("PHOTO_DEBUG", "archivo existe: ${photoPath?.let { File(it).exists() }}")
    }

    // ← photoUri derivado del path guardado en Room
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
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            // ← guarda el path que luego Room va a persistir
            imageFilePath?.let { path ->
                photoPath = path
                android.util.Log.d("PHOTO_DEBUG", "Nueva foto guardada en: $path")
            }
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val file = createImageFile()
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
            cameraLauncher.launch(uri)
        }
    }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Editar Medicamento",
            style = MaterialTheme.typography.titleLarge
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = quantity,
            onValueChange = { quantity = it },
            label = { Text("Cantidad") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Precio") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // ← muestra foto desde Room si existe
                if (photoUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(photoUri),
                        contentDescription = "Foto del medicamento",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text(
                        text = "Sin foto",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }

                Button(
                    onClick = {
                        permissionLauncher.launch(android.Manifest.permission.CAMERA)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Camera,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(if (photoUri == null) "📷 Agregar foto" else "📷 Cambiar foto")
                }
            }
        }

        Button(
            onClick = {
                // ← photoPath se pasa al ViewModel que lo guarda en Room
                android.util.Log.d("PHOTO_DEBUG", "Guardando con photoPath: $photoPath")
                onUpdate(name, quantity, price, description, photoPath)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Actualizar Medicamento")
        }
    }
}