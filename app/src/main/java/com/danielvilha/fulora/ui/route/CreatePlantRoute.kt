package com.danielvilha.fulora.ui.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.danielvilha.fulora.ui.create.CreatePlantScreen
import com.danielvilha.fulora.ui.create.CreatePlantViewModel

@Composable
fun CreatePlantRoute(
    onBackClick: () -> Unit,
    onSavedSuccess: () -> Unit,
    viewModel: CreatePlantViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val events = viewModel.events

    CreatePlantScreen(
        state = state,
        onEvent = viewModel::onEvent,
        events = events,
        onBackClick = onBackClick,
        onSaveSuccess = onSavedSuccess,
    )
}