package com.danielvilha.fulora.service

import androidx.compose.ui.text.toUpperCase
import com.danielvilha.fulora.data.Plant
import com.danielvilha.fulora.data.PlantDao
import com.danielvilha.fulora.data.remote.SpeciesDto
import kotlinx.coroutines.flow.Flow
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlantRepository @Inject constructor(
    private val apiService: ApiService,
    private val plantDao: PlantDao
) {

    fun getAllPlants(): Flow<List<Plant>> = plantDao.getAll()

    suspend fun createPlant(plant: Plant) {
        plantDao.insert(plant)
    }

    suspend fun searchAndSavePlant(plantName: String) {
        try {
            val response = apiService.searchSpecies(plantName)
            val species = response.species.firstOrNull()
            species?.let { speciesDto ->
                val plant = mapSpeciesToPlant(speciesDto)
                plantDao.insert(plant)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun searchPlant(plantName: String) : Plant? {
        return try {
            val response = apiService.searchSpecies(plantName)
            val species = response.species.firstOrNull()
            species?.let {
                mapSpeciesToPlant(it)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun mapSpeciesToPlant(speciesDto: SpeciesDto): Plant {
        return Plant(
            specieId = speciesDto.id,
            name = speciesDto.commonName.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.getDefault()
                ) else it.toString()
            },
            speciesFamily = speciesDto.family ?: speciesDto.scientificName.firstOrNull(),
            location = "",
            wateringIntervalDays = mapWateringToDays(speciesDto.watering),
            fertilizingIntervalDays = mapFertilizingToDays(speciesDto.cultivar),
            reportingIntervalMonths = mapReportingToMonths(speciesDto.watering),
            lastWateredDate = System.currentTimeMillis(),
            lastFertilizedDate = System.currentTimeMillis(),
            lastRepottedDate = System.currentTimeMillis(),
            imageUrl = speciesDto.defaultImage?.originalUrl ?: ""
        )
    }

    private fun mapWateringToDays(watering: String?): Int {
        return when (watering?.lowercase()) {
            "frequent" -> 3
            "average" -> 7
            "minimum" -> 14
            "none" -> 30
            else -> 7
        }
    }

    private fun mapFertilizingToDays(frequency: String?): Int {
        return when (frequency?.lowercase()) {
            "frequent" -> 30
            "average" -> 70
            "minimum" -> 140
            "none" -> 300
            else -> 70
        }
    }

    private fun mapReportingToMonths(reporting: String?): Int {
        return when (reporting?.lowercase()) {
            "frequent" -> 3
            "average" -> 7
            "minimum" -> 12
            "none" -> 24
            else -> 7
        }
    }
}
