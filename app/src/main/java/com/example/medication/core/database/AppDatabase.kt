package com.example.medication.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.medication.core.database.dao.FavoriteDao
import com.example.medication.core.database.dao.MedicationDao
import com.example.medication.core.database.dao.SearchMedicineDao
import com.example.medication.core.database.entities.FavoriteEntity
import com.example.medication.core.database.entities.MedicationEntity
import com.example.medication.core.database.entities.SearchMedicineEntity

@Database(
    entities = [
        MedicationEntity::class,
        FavoriteEntity::class,
        SearchMedicineEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun medicationDao(): MedicationDao

    abstract fun favoriteDao(): FavoriteDao

    abstract fun searchMedicineDao(): SearchMedicineDao
}