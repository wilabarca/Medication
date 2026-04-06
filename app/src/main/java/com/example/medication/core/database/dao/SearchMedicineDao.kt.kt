package com.example.medication.core.database.dao

import androidx.room.*
import com.example.medication.core.database.entities.SearchMedicineEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchMedicineDao {

    @Query("""
        SELECT * FROM search_medicines_cache 
        WHERE name LIKE '%' || :query || '%' 
        OR activeIngredient LIKE '%' || :query || '%' 
        ORDER BY name ASC
    """)
    fun searchByNameOrIngredient(query: String): Flow<List<SearchMedicineEntity>>

    @Query("SELECT * FROM search_medicines_cache WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): SearchMedicineEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(medicines: List<SearchMedicineEntity>)

    @Query("DELETE FROM search_medicines_cache")
    suspend fun clearAll()

    @Query("SELECT COUNT(*) FROM search_medicines_cache")
    suspend fun count(): Int
}