package com.example.medication.features.searchmedicines.data.repository

import com.example.medication.core.database.dao.SearchMedicineDao
import com.example.medication.core.database.entities.SearchMedicineEntity
import com.example.medication.features.searchmedicines.data.remote.api.MedicineApiService
import com.example.medication.features.searchmedicines.data.remote.dto.MedicineDto
import com.example.medication.features.searchmedicines.domain.entities.Medicine
import com.example.medication.features.searchmedicines.domain.repositories.MedicineRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class MedicineRepositoryImpl @Inject constructor(
    private val dao: SearchMedicineDao,
    private val api: MedicineApiService
) : MedicineRepository {

    override fun searchMedicines(query: String): Flow<List<Medicine>> {
        return dao.searchByNameOrIngredient(query)
            .onStart {
                if (dao.count() == 0) fetchAndCache()
            }
            .map { it.map { entity -> entity.toDomain() } }
    }

    override suspend fun getMedicineById(id: Int): Medicine? {
        return dao.getById(id)?.toDomain()
    }

    override suspend fun clearCache() {
        dao.clearAll()
    }

    private suspend fun fetchAndCache() {
        try {
            val response = api.getAllMedicines()
            dao.insertAll(response.map { it.toEntity() })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun MedicineDto.toEntity() = SearchMedicineEntity(
        id = id,
        name = name,
        activeIngredient = activeIngredient,
        presentation = presentation,
        dosage = dosage,
        requiresPrescription = requiresPrescription,
        description = description
    )

    private fun SearchMedicineEntity.toDomain() = Medicine(
        id = id,
        name = name,
        activeIngredient = activeIngredient,
        presentation = presentation,
        dosage = dosage,
        requiresPrescription = requiresPrescription,
        description = description
    )
}