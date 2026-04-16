package com.example.medication.core.hardware.data

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.medication.core.database.entities.MedicationAlarmEntity
import com.example.medication.core.hardware.domain.MedicationAlarmScheduler
import com.example.medication.features.medication.data.alarm.AlarmReceiver
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Calendar
import javax.inject.Inject

class AndroidAlarmManager @Inject constructor(
    @ApplicationContext private val context: Context
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

        if (alarm.selectedWeekDays.isNotEmpty()) {
            alarm.selectedWeekDays.distinct().forEach { day ->
                scheduleWeekly(alarm, day)
            }
            return
        }

        if (alarm.intervalHours > 0) {
            scheduleInterval(alarm)
            return
        }

        scheduleOneTime(alarm)
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

        val pendingIntent = buildIntent(
            requestCode = baseRequestCode(alarm.id),
            alarmId = alarm.id,
            medicationName = alarm.medicationName,
            intervalHours = 0,
            weekDay = -1
        )

        setExact(calendar.timeInMillis, pendingIntent)
    }

    private fun scheduleInterval(alarm: MedicationAlarmEntity) {
        val intervalMillis = alarm.intervalHours * 60L * 60L * 1000L

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis() + intervalMillis
        }

        val pendingIntent = buildIntent(
            requestCode = baseRequestCode(alarm.id),
            alarmId = alarm.id,
            medicationName = alarm.medicationName,
            intervalHours = alarm.intervalHours,
            weekDay = -1
        )

        setExact(calendar.timeInMillis, pendingIntent)
    }

    private fun scheduleWeekly(alarm: MedicationAlarmEntity, dayOfWeekIndex: Int) {
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

        val requestCode = weeklyRequestCode(alarm.id, dayOfWeekIndex)

        val pendingIntent = buildIntent(
            requestCode = requestCode,
            alarmId = alarm.id,
            medicationName = alarm.medicationName,
            intervalHours = 0,
            weekDay = dayOfWeekIndex
        )

        setExact(calendar.timeInMillis, pendingIntent)
    }

    private fun setExact(triggerAtMillis: Long, pendingIntent: PendingIntent) {
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
            medicationName = "",
            intervalHours = 0,
            weekDay = -1
        )
        alarmManager.cancel(pendingIntent)
    }

    private fun cancelDayIntent(alarmId: Long, dayOfWeekIndex: Int) {
        val pendingIntent = buildIntent(
            requestCode = weeklyRequestCode(alarmId, dayOfWeekIndex),
            alarmId = alarmId,
            medicationName = "",
            intervalHours = 0,
            weekDay = dayOfWeekIndex
        )
        alarmManager.cancel(pendingIntent)
    }

    private fun buildIntent(
        requestCode: Int,
        alarmId: Long,
        medicationName: String,
        intervalHours: Int,
        weekDay: Int
    ): PendingIntent {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            action = "MEDICATION_ALARM_$requestCode"
            putExtra("alarm_id", alarmId)
            putExtra("medication_name", medicationName)
            putExtra("interval_hours", intervalHours)
            putExtra("week_day", weekDay)
        }

        return PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun baseRequestCode(alarmId: Long) = (alarmId % Int.MAX_VALUE).toInt()

    private fun weeklyRequestCode(alarmId: Long, dayOfWeekIndex: Int) =
        ((alarmId * 10) + dayOfWeekIndex).toInt()
}