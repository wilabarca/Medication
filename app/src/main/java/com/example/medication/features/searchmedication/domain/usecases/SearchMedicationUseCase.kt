package com.example.medication.features.searchmedication.domain.usecases

import com.example.medication.features.searchmedication.domain.entities.Medication
import com.example.medication.features.searchmedication.domain.repositories.MedicationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchMedicationUseCase @Inject constructor(
    private val repository: MedicationRepository
) {
    operator fun invoke(query: String): Flow<List<Medication>> {
        if (query.isBlank() || query.length < 2) return flow { emit(emptyList()) }
        return repository.searchMedicines(query.trim())
    }
}