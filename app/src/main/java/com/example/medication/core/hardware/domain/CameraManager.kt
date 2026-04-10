package com.example.medication.core.hardware.domain

interface CameraManager {
    fun hasCamera(): Boolean
    fun createImageFile(filesDir: java.io.File): java.io.File
}