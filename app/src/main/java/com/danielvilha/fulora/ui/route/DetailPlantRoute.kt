package com.danielvilha.fulora.ui.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.danielvilha.fulora.ui.plant.PlantScreen
import com.danielvilha.fulora.ui.plant.PlantViewModel

@Composable
fun DetailPlantRoute(
    onBackClick: () -> Unit,
    viewModel: PlantViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsState()

    PlantScreen(
        state = state,
        onBackClick = onBackClick,
        onWaterPlant = viewModel::onWaterPlant,
        onFertilizePlant = viewModel::onFertilizePlant,
        onRepotPlant = viewModel::onRepotPlant,
    )
}
