package com.example.medication.core.hardware.data

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.medication.core.database.entities.MedicationAlarmEntity
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
        } else {
            true
        }
    }

    override fun scheduleAlarm(alarm: MedicationAlarmEntity) {
        if (!alarm.isEnabled) return

        when {
            alarm.selectedWeekDays.isNotEmpty() -> {
                alarm.selectedWeekDays.distinct().forEach { day ->
                    scheduleWeeklyNext(alarm, day)
                }
            }
            alarm.intervalHours > 0 -> {
                scheduleIntervalNext(alarm)
            }
            else -> {
                scheduleOneTime(alarm)
            }
        }
    }

    override fun cancelAlarm(alarm: MedicationAlarmEntity) {
        cancelBaseIntent(alarm.id)
        alarm.selectedWeekDays.distinct().forEach { day ->
            cancelDayIntent(alarm.id, day)
        }
    }

    private fun scheduleOneTime(alarm: MedicationAlarmEntity) {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, alarm.startHour)
            set(Calendar.MINUTE, alarm.startMinute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        scheduleExact(
            requestCode = baseRequestCode(alarm.id),
            triggerAtMillis = calendar.timeInMillis,
            alarmId = alarm.id,
            medicationName = alarm.medicationName
        )
    }

    private fun scheduleIntervalNext(alarm: MedicationAlarmEntity) {
        val intervalMillis = alarm.intervalHours * 60L * 60L * 1000L

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, alarm.startHour)
            set(Calendar.MINUTE, alarm.startMinute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            while (timeInMillis <= System.currentTimeMillis()) {
                timeInMillis += intervalMillis
            }
        }

        scheduleExact(
            requestCode = baseRequestCode(alarm.id),
            triggerAtMillis = calendar.timeInMillis,
            alarmId = alarm.id,
            medicationName = alarm.medicationName
        )
    }

    private fun scheduleWeeklyNext(alarm: MedicationAlarmEntity, dayOfWeekIndex: Int) {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_WEEK, dayOfWeekIndex + 1)
            set(Calendar.HOUR_OF_DAY, alarm.startHour)
            set(Calendar.MINUTE, alarm.startMinute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.WEEK_OF_YEAR, 1)
            }
        }

        scheduleExact(
            requestCode = weeklyRequestCode(alarm.id, dayOfWeekIndex),
            triggerAtMillis = calendar.timeInMillis,
            alarmId = alarm.id,
            medicationName = alarm.medicationName
        )
    }

    private fun scheduleExact(
        requestCode: Int,
        triggerAtMillis: Long,
        alarmId: Long,
        medicationName: String
    ) {
        val pendingIntent = buildIntent(
            requestCode = requestCode,
            alarmId = alarmId,
            medicationName = medicationName
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerAtMillis,
                pendingIntent
            )
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerAtMillis,
                pendingIntent
            )
        } else {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                triggerAtMillis,
                pendingIntent
            )
        }
    }

    private fun cancelBaseIntent(alarmId: Long) {
        val pendingIntent = buildIntent(
            requestCode = baseRequestCode(alarmId),
            alarmId = alarmId,
            medicationName = ""
        )
        alarmManager.cancel(pendingIntent)
    }

    private fun cancelDayIntent(alarmId: Long, dayOfWeekIndex: Int) {
        val pendingIntent = buildIntent(
            requestCode = weeklyRequestCode(alarmId, dayOfWeekIndex),
            alarmId = alarmId,
            medicationName = ""
        )
        alarmManager.cancel(pendingIntent)
    }

    private fun buildIntent(
        requestCode: Int,
        alarmId: Long,
        medicationName: String
    ): PendingIntent {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            action = "MEDICATION_ALARM_$requestCode"
            putExtra("alarm_id", alarmId)
            putExtra("medication_name", medicationName)
        }

        return PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun baseRequestCode(alarmId: Long): Int {
        return (alarmId % Int.MAX_VALUE).toInt()
    }

    private fun weeklyRequestCode(alarmId: Long, dayOfWeekIndex: Int): Int {
        return ((alarmId * 10) + dayOfWeekIndex).toInt()
    }
}