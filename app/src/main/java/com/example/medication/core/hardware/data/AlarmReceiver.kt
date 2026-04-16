package com.example.medication.features.medication.data.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.medication.MainActivity
import com.example.medication.R
import com.example.medication.core.database.dao.MedicationAlarmDao
import com.example.medication.core.hardware.data.AndroidAlarmManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var alarmDao: MedicationAlarmDao

    @Inject
    lateinit var alarmScheduler: AndroidAlarmManager

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onReceive(context: Context, intent: Intent) {
        val medicationName = intent.getStringExtra("medication_name") ?: "Medicamento"
        val alarmId = intent.getLongExtra("alarm_id", 0L)
        val intervalHours = intent.getIntExtra("interval_hours", 0)
        val weekDay = intent.getIntExtra("week_day", -1)

        createNotificationChannel(context)
        showNotification(context, medicationName, alarmId)

        scope.launch {
            rescheduleAlarm(alarmId, intervalHours, weekDay)
        }
    }

    private suspend fun rescheduleAlarm(alarmId: Long, intervalHours: Int, weekDay: Int) {
        val alarm = alarmDao.getAlarmById(alarmId) ?: return

        if (!alarm.isEnabled) return

        if (intervalHours > 0) {
            alarmScheduler.scheduleAlarm(alarm)
            return
        }

        if (weekDay >= 0) {
            alarmScheduler.scheduleAlarm(alarm)
            return
        }
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Alarmas de Medicamentos",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Recordatorios de medicamentos"
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 500, 200, 500)
            }

            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun showNotification(context: Context, medicationName: String, alarmId: Long) {
        val openAppIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            alarmId.toInt(),
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("💊 Hora de tu medicamento")
            .setContentText("Es hora de tomar: $medicationName")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(0, 500, 200, 500))
            .setContentIntent(pendingIntent)
            .build()

        val manager = context.getSystemService(NotificationManager::class.java)
        manager.notify(alarmId.toInt(), notification)
    }

    companion object {
        const val CHANNEL_ID = "medication_alarm_channel"
    }
}