package com.example.medication.features.medication.data.datasources.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.medication.features.medication.data.datasources.local.alarm.dao.MedicationAlarmDao
import com.example.medication.features.medication.data.datasources.local.alarm.entities.MedicationAlarmEntity
import com.example.medication.features.medication.data.datasources.local.converters.AlarmConverters


@Database(
    entities = [MedicationAlarmEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(AlarmConverters::class)
abstract class MedicationAlarmDatabase: RoomDatabase(){
    abstract fun medicationAlarmDao(): MedicationAlarmDao
}
