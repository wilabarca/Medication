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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedication(medication: MedicationEntity)

    @Query("SELECT * FROM medications")
    suspend fun getAllMedications(): List<MedicationEntity>

    @Query("SELECT * FROM medications WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): MedicationEntity?

    @Query("SELECT * FROM medications WHERE name LIKE '%' || :query || '%'")
    suspend fun searchByName(query: String): List<MedicationEntity>

    @Query("DELETE FROM medications")
    suspend fun clearAll()

    @Query("DELETE FROM medications WHERE id = :id")  // ← agregar
    suspend fun deleteById(id: String)

    @Query("UPDATE medications SET photoPath = :photoPath WHERE id = :id")
    suspend fun updatePhotoPath(id: String, photoPath: String?)
}