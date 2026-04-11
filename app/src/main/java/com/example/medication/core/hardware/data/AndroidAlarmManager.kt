package com.example.medication.core.hardware.data

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.medication.core.hardware.domain.MedicationAlarmScheduler
import com.example.medication.features.medication.data.alarm.AlarmReceiver
import java.util.Calendar
import javax.inject.Inject

class AndroidAlarmManager @Inject constructor(
    private val context: Context
) : MedicationAlarmScheduler {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun canScheduleExact(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else true
    }

    override fun scheduleAlarm(
        alarmId: Long,
        medicationName: String,
        hour: Int,
        minute: Int,
        selectedDays: List<Int>
    ) {
        android.util.Log.d("ALARM_DEBUG", "🔔 Programando: $medicationName a las $hour:$minute")
        android.util.Log.d("ALARM_DEBUG", "🔔 Días: $selectedDays")

        if (selectedDays.isEmpty()) {
            scheduleOneTime(alarmId, medicationName, hour, minute)
        } else {
            selectedDays.forEach { day ->
                scheduleWeekly(alarmId, medicationName, hour, minute, day)
            }
        }
    }

    override fun cancelAlarm(alarmId: Long) {
        val intent = buildIntent(alarmId, "")
        alarmManager.cancel(intent)
        android.util.Log.d("ALARM_DEBUG", "❌ Alarma $alarmId cancelada")
    }

    private fun scheduleOneTime(
        alarmId: Long,
        medicationName: String,
        hour: Int,
        minute: Int
    ) {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (timeInMillis <= System.currentTimeMillis()) add(Calendar.DAY_OF_YEAR, 1)
        }
        android.util.Log.d("ALARM_DEBUG", "🔔 Alarma única para: ${calendar.time}")
        setExactAlarm(alarmId, calendar.timeInMillis, buildIntent(alarmId, medicationName))
    }

    private fun scheduleWeekly(
        alarmId: Long,
        medicationName: String,
        hour: Int,
        minute: Int,
        dayOfWeek: Int
    ) {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_WEEK, dayOfWeek + 1)
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (timeInMillis <= System.currentTimeMillis()) add(Calendar.WEEK_OF_YEAR, 1)
        }
        val uniqueId = alarmId * 10 + dayOfWeek
        android.util.Log.d("ALARM_DEBUG", "🔔 Alarma semanal día $dayOfWeek para: ${calendar.time}")
        setExactAlarm(uniqueId, calendar.timeInMillis, buildIntent(uniqueId, medicationName))
    }

    private fun setExactAlarm(id: Long, triggerAtMillis: Long, pendingIntent: PendingIntent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && alarmManager.canScheduleExactAlarms()) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerAtMillis,
                pendingIntent
            )
            android.util.Log.d("ALARM_DEBUG", "✅ Alarma exacta programada id=$id")
        } else {
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerAtMillis,
                pendingIntent
            )
            android.util.Log.d("ALARM_DEBUG", "⚠️ Alarma inexacta id=$id")
        }
    }

    private fun buildIntent(alarmId: Long, medicationName: String): PendingIntent {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            action = "MEDICATION_ALARM_$alarmId"
            putExtra("alarm_id", alarmId)
            putExtra("medication_name", medicationName)
        }
        return PendingIntent.getBroadcast(
            context,
            (alarmId % Int.MAX_VALUE).toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}