package com.danielvilha.fulora.ui.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danielvilha.fulora.data.Plant
import com.danielvilha.fulora.service.ApiService
import com.danielvilha.fulora.service.PlantRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

interface CreatePlantEvent {
    data object SaveSuccess : CreatePlantEvent
}

@HiltViewModel
class CreatePlantViewModel @Inject constructor(
    private val apiService: ApiService,
    private val plantRepository: PlantRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        CreatePlantUiState(
            plant = Plant(
                name = "",
                specieId = null,
                speciesFamily = null,
                location = "",
                wateringIntervalDays = 0,
                fertilizingIntervalDays = 0,
                reportingIntervalMonths = 0,
                lastWateredDate = 0,
                lastFertilizedDate = 0,
                lastRepottedDate = 0,
                imageUrl = null
            )
        )
    )
    val uiState: StateFlow<CreatePlantUiState> = _uiState.asStateFlow()

    private val _events = Channel<CreatePlantEvent>()
    val events = _events.receiveAsFlow()


    fun onEvent(event: CreatePlantUiEvent) {
        when (event) {
            is CreatePlantUiEvent.OnSearchQueryChange -> onSearchQueryChange(event.searchQuery)
            is CreatePlantUiEvent.OnPlantNameChange -> onPlantNameChange(event.plantName)
            is CreatePlantUiEvent.OnWateringFrequencyChange -> onWateringFrequencyChange(event.wateringFrequency)
            is CreatePlantUiEvent.OnSpeciesSelectedChange -> onSpeciesChange(event.species)
            is CreatePlantUiEvent.OnFertilizingFrequencyChange -> onFertilizingFrequencyChange(event.fertilizingFrequency)
            is CreatePlantUiEvent.OnPlantLocationChange -> onPlantLocationChange(event.plantLocation)
            is CreatePlantUiEvent.OnReportingFrequencyChange -> onRepottingFrequencyChange(event.reportingFrequency)
            is CreatePlantUiEvent.OnSearchClicked -> searchSpecies(event.searchQuery)
            is CreatePlantUiEvent.OnSaveClicked,
            is CreatePlantUiEvent.OnRetryClicked -> onSaveClicked()
        }
    }

    private fun onRepottingFrequencyChange(reportingFrequency: String) {
        _uiState.update {
            it.copy(plant = it.plant?.copy(reportingIntervalMonths = reportingFrequency.toIntOrNull() ?: 0))
        }
    }

    private fun onPlantLocationChange(plantLocation: String) {
        _uiState.update {
            it.copy(plant = it.plant?.copy(location = plantLocation))
        }
    }

    private fun onFertilizingFrequencyChange(fertilizingFrequency: String) {
        _uiState.update {
            it.copy(plant = it.plant?.copy(fertilizingIntervalDays = fertilizingFrequency.toIntOrNull() ?: 0))
        }
    }

    private fun onPlantNameChange(name: String) {
        _uiState.update {
            it.copy(plant = it.plant?.copy(name = name))
        }
    }

    private fun onWateringFrequencyChange(frequency: String) {
        _uiState.update {
            it.copy(plant = it.plant?.copy(wateringIntervalDays = frequency.toIntOrNull() ?: 0))
        }
    }

    private fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        if (query.length > 2) {
            searchSpecies(query)
        }
    }

    private fun searchSpecies(query: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSearching = true) }
            try {
                val response = apiService.searchSpecies(query = query)
                _uiState.update { it.copy(searchResults = response, isSearching = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message ?: "Erro desconhecido", isSearching = false) }
            }
        }
    }

    private fun onSpeciesChange(speciesName: String) {
        viewModelScope.launch {
            try {
                val speciesPlantData = plantRepository.searchPlant(speciesName)
                _uiState.update { currentState ->
                    val currentEditedPlant = currentState.plant
                    val mergedPlant = speciesPlantData?.copy(
                        name = currentEditedPlant?.name?.takeIf { it.isNotBlank() } ?: speciesPlantData.name,
                        location = currentEditedPlant?.location ?: speciesPlantData.location,
                    )
                    currentState.copy(
                        plant = mergedPlant,
                        searchQuery = "",
                        searchResults = null
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun onSaveClicked() {
        val currentPlant = _uiState.value.plant
        if (currentPlant?.name?.isBlank() == true || currentPlant?.wateringIntervalDays == 0) {
            _uiState.update { it.copy(errorMessage = "Please fill in all fields.") }
            return
        }

        viewModelScope.launch {
            if (currentPlant != null) {
                try {
                    plantRepository.createPlant(currentPlant)
                    _uiState.update { it.copy(isSaved = true) }
                    _events.send(CreatePlantEvent.SaveSuccess)
                } catch (e: Exception) {
                    _uiState.update { it.copy(errorMessage = e.message ?: "Failed to save plant.") }
                }
            }
        }
    }
}
