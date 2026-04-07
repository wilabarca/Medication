package com.example.medication.features.searchmedication.data.repositories

import com.example.medication.core.database.dao.SearchMedicineDao
import com.example.medication.features.searchmedication.data.datasources.local.mapper.toDomain
import com.example.medication.features.searchmedication.data.datasources.remote.api.MedicationApiService
import com.example.medication.features.searchmedication.data.datasources.remote.mapper.toEntity
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

    // Flow reactivo — igual que getPosts() del profe
    override fun searchMedicines(query: String): Flow<List<Medication>> {
        return dao.searchByName(query)
            .onStart {
                if (dao.count() == 0) syncMedications()
            }
            .map { entities -> entities.map { it.toDomain() } }
    }

    // Sync separado — igual que syncPosts() del profe
    override suspend fun syncMedications() {
        try {
            val response = api.getAllMedicines()
            dao.insertAll(response.map { it.toEntity() })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun getMedicineById(id: String): Medication? {
        return dao.getById(id)?.toDomain()
    }

    override suspend fun clearCache() {
        dao.clearAll()
    }
}