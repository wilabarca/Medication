package com.example.medication.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.medication.core.database.dao.FavoriteDao
import com.example.medication.core.database.dao.MedicationDao
import com.example.medication.core.database.dao.MedicationAlarmDao
import com.example.medication.core.database.dao.MedicationHistoryDao
import com.example.medication.core.database.dao.SearchMedicineDao
import com.example.medication.core.database.entities.FavoriteEntity
import com.example.medication.core.database.entities.MedicationAlarmEntity
import com.example.medication.core.database.entities.MedicationEntity
import com.example.medication.core.database.entities.MedicationHistoryEntity
import com.example.medication.core.database.entities.SearchMedicineEntity
import com.example.medication.features.medication.data.datasources.local.converters.AlarmConverters

@Database(
    entities = [
        MedicationEntity::class,
        FavoriteEntity::class,
        SearchMedicineEntity::class,
        MedicationAlarmEntity::class,
        MedicationHistoryEntity::class  // ← nuevo
    ],
    version = 6,                        // ← incrementa de 5 a 6
    exportSchema = false
)
@TypeConverters(AlarmConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun medicationDao(): MedicationDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun searchMedicineDao(): SearchMedicineDao
    abstract fun medicationAlarmDao(): MedicationAlarmDao
    abstract fun medicationHistoryDao(): MedicationHistoryDao  // ← nuevo
}