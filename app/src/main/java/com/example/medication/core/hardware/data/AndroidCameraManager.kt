package com.example.medication.core.hardware.data

import android.content.Context
import android.content.pm.PackageManager
import com.example.medication.core.hardware.domain.CameraManager
import java.io.File
import javax.inject.Inject

class AndroidCameraManager @Inject constructor(
    private val context: Context
) : CameraManager {

    override fun hasCamera(): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
    }

    override fun createImageFile(filesDir: File): File {
        val dir = File(filesDir, "medication_photos")
        if (!dir.exists()) dir.mkdirs()
        return File(dir, "med_${System.currentTimeMillis()}.jpg")
    }
}