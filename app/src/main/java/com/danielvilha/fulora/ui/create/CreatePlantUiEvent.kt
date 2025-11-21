package com.danielvilha.fulora.ui.create

sealed class CreatePlantUiEvent {
    data class OnPlantNameChange(val plantName : String) : CreatePlantUiEvent()
    data class OnSearchQueryChange(val searchQuery : String) : CreatePlantUiEvent()
    data class OnWateringFrequencyChange(val wateringFrequency : String) : CreatePlantUiEvent()
    data class OnSpeciesSelectedChange(val species : String) : CreatePlantUiEvent()
    data class OnReportingFrequencyChange(val reportingFrequency : String) : CreatePlantUiEvent()
    data class OnFertilizingFrequencyChange(val fertilizingFrequency : String) : CreatePlantUiEvent()
    data class OnPlantLocationChange(val plantLocation : String) : CreatePlantUiEvent()
    data class OnSearchClicked(val searchQuery : String) : CreatePlantUiEvent()
    data object OnSaveClicked : CreatePlantUiEvent()
    data object OnRetryClicked : CreatePlantUiEvent()
}
