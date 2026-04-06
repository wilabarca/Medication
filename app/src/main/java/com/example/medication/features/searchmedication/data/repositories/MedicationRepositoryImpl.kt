package com.example.medication.features.searchmedication.data.repository

import com.example.medication.core.database.dao.SearchMedicineDao
import com.example.medication.core.database.entities.SearchMedicineEntity
import com.example.medication.features.searchmedication.data.remote.api.MedicationApiService
import com.example.medication.features.searchmedication.data.remote.dto.MedicationDto
import com.example.medication.features.searchmedication.domain.entities.Medication
import com.example.medication.features.searchmedication.domain.repositories.MedicationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class MedicationRepositoryImpl @Inject constructor(
    private val dao: SearchMedicineDao,
    private val api: MedicationApiService
) : MedicationRepository {

    override fun searchMedicines(query: String): Flow<List<Medication>> {
        android.util.Log.d("SEARCH_DEBUG", "🔍 Query: '$query'")
        return dao.searchByName(query)
            .onStart {
                val count = dao.count()
                android.util.Log.d("SEARCH_DEBUG", "📊 Cache actual: $count items")
                if (count == 0) {
                    android.util.Log.d("SEARCH_DEBUG", "📡 Cache vacío, llamando API...")
                    fetchAndCache()
                    android.util.Log.d("SEARCH_DEBUG", "✅ Cache poblado: ${dao.count()} items")
                }
            }
            .map { entities ->
                android.util.Log.d("SEARCH_DEBUG", "📦 Room devuelve: ${entities.size} para '$query'")
                entities.map { it.toDomain() }
            }
    }

    override suspend fun ensureCacheLoaded() {
        val count = dao.count()
        android.util.Log.d("SEARCH_DEBUG", "📊 Cache actual: $count items")
        if (count == 0) fetchAndCache()
    }

    override suspend fun getMedicineById(id: String): Medication? {
        return dao.getById(id)?.toDomain()
    }

    override suspend fun clearCache() {
        dao.clearAll()
    }

    private suspend fun fetchAndCache() {
        try {
            android.util.Log.d("SEARCH_DEBUG", "📡 Llamando a la API...")
            val response = api.getAllMedicines()
            android.util.Log.d("SEARCH_DEBUG", "✅ API response: ${response.size} items")
            android.util.Log.d("SEARCH_DEBUG", "✅ Primer item: ${response.firstOrNull()}")
            dao.insertAll(response.map { it.toEntity() })
            android.util.Log.d("SEARCH_DEBUG", "✅ Room count después de insertar: ${dao.count()}")
        } catch (e: Exception) {
            android.util.Log.e("SEARCH_DEBUG", "❌ Error en fetchAndCache: ${e.message}", e)
        }
    }

    // ← price se convierte de String a Double
    private fun MedicationDto.toEntity() = SearchMedicineEntity(
        id = id,
        name = name,
        description = description,
        quantity = quantity,
        price = price.toDoubleOrNull() ?: 0.0
    )

    private fun SearchMedicineEntity.toDomain() = Medication(
        id = id,
        name = name,
        description = description,
        quantity = quantity,
        price = price
    )
}