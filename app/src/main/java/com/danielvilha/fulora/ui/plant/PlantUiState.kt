package com.danielvilha.fulora.ui.plant

import com.danielvilha.fulora.data.Plant

data class PlantUiState(
    var plant: Plant,
    var isLoading: Boolean = false,
)