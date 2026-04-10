package com.example.medication.core.hardware.domain

interface VibrateManager {
    fun vibrate(durationMs: Long = 60L)
}