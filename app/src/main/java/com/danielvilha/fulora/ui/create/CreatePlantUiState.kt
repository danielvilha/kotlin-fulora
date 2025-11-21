package com.danielvilha.fulora.ui.create

import com.danielvilha.fulora.data.Plant
import com.danielvilha.fulora.data.remote.SpeciesListResponse

data class CreatePlantUiState(
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val isEnabled: Boolean = true,
    var isSearching: Boolean = false,
    val plant: Plant? = null,
    var searchQuery: String = "",
    var searchResults: SpeciesListResponse? = null,
    var errorMessage: String? = null
)
