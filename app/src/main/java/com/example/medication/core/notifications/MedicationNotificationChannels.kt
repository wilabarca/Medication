package com.example.medication.core.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

object MedicationNotificationChannels {

    const val MEDICATIONS_CHANNEL_ID = "medications_channel"
    private const val MEDICATIONS_CHANNEL_NAME = "Medicamentos"
    private const val MEDICATIONS_CHANNEL_DESCRIPTION =
        "Notificaciones cuando se registra un medicamento"

    fun create(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                MEDICATIONS_CHANNEL_ID,
                MEDICATIONS_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = MEDICATIONS_CHANNEL_DESCRIPTION
                enableVibration(true)
            }

            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
}