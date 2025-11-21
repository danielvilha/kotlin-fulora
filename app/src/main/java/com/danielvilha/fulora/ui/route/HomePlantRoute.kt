package com.danielvilha.fulora.ui.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.danielvilha.fulora.data.Plant
import com.danielvilha.fulora.ui.home.HomeScreen
import com.danielvilha.fulora.ui.home.HomeViewModel

@Composable
fun HomePlantRoute(
    onPlantClick: (Plant) -> Unit,
    onCreatePlantClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getPlantList()
    }

    HomeScreen(
        state = state,
        onPlantClick = onPlantClick,
        onCreatePlantClick = onCreatePlantClick,
        onRetry = { viewModel.getPlantList() },
    )
}