package com.example.medication.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.medication.core.database.entities.MedicationEntity

@Dao
interface MedicationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(medications: List<MedicationEntity>)

    @Query("SELECT * FROM medications WHERE name LIKE '%' || :query || '%'")
    suspend fun searchByName(query: String): List<MedicationEntity>

    @Query("DELETE FROM medications")
    suspend fun clearAll()
}