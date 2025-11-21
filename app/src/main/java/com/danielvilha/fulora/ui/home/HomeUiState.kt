package com.danielvilha.fulora.ui.home

import com.danielvilha.fulora.data.Plant

data class HomeUiState(
    val isLoading: Boolean = false,
    var gardenPlants: List<Plant> = emptyList(),
    var errorMessage: String? = null
)
