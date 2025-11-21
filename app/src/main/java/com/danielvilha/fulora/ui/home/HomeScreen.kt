package com.danielvilha.fulora.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.danielvilha.fulora.R
import com.danielvilha.fulora.data.Plant
import com.danielvilha.fulora.ui.preview.ExcludeFromJacocoGeneratedReport
import com.danielvilha.fulora.ui.preview.LightDarkPreview
import com.danielvilha.fulora.ui.theme.FuloraTheme
import com.danielvilha.fulora.util.ErrorText
import com.danielvilha.fulora.util.ProgressIndicator
import java.util.concurrent.TimeUnit

@Composable
@LightDarkPreview
@ExcludeFromJacocoGeneratedReport
private fun HomeScreenPreview(
    @PreviewParameter(HomeScreenProvider::class)
    state: HomeUiState
) {
    FuloraTheme {
        HomeScreen(
            state = state,
            onPlantClick = {},
            onCreatePlantClick = {},
            onRetry = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeUiState,
    onPlantClick: (Plant) -> Unit,
    onCreatePlantClick: () -> Unit,
    onRetry: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onCreatePlantClick) {
                Icon(Icons.Filled.Add, contentDescription = "Create Plant")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            when {
                state.isLoading -> ProgressIndicator()
                state.errorMessage != null -> ErrorText(error = state.errorMessage, onRetry = onRetry)
                else -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(state.gardenPlants) { plant ->
                            PlantListItem(plant = plant, onPlantClick = onPlantClick)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PlantListItem(plant: Plant, onPlantClick: (Plant) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        onClick = { onPlantClick(plant) }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            WateringStatusIndicator(plant = plant)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = plant.name)
                Text(text = plant.speciesFamily ?: "")
            }
        }
    }
}

@Composable
fun WateringStatusIndicator(plant: Plant) {
    val color = getWateringStatusColor(plant)
    Box(
        modifier = Modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(color)
    )
}

private fun getWateringStatusColor(plant: Plant): Color {
    val currentTime = System.currentTimeMillis()
    val lastWatered = plant.lastWateredDate
    val intervalDays = plant.wateringIntervalDays

    if (intervalDays <= 0) return Color.Gray

    val nextWateringTime = lastWatered + TimeUnit.DAYS.toMillis(intervalDays.toLong())
    val twoDaysBefore = nextWateringTime - TimeUnit.DAYS.toMillis(2)

    return when {
        currentTime >= nextWateringTime -> Color.Red
        currentTime >= twoDaysBefore -> Color.Yellow
        else -> Color.Green
    }
}

@ExcludeFromJacocoGeneratedReport
class HomeScreenProvider : PreviewParameterProvider<HomeUiState> {
    override val values: Sequence<HomeUiState>
        get() = sequenceOf(
            HomeUiState(
                isLoading = false,
                gardenPlants = listOf(
                    Plant(
                        id = 1,
                        specieId = 1,
                        name = "Orquídea",
                        speciesFamily = "Orchidaceae",
                        wateringIntervalDays = 7,
                        lastWateredDate = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1),
                        fertilizingIntervalDays = 0,
                        reportingIntervalMonths = 0,
                        lastFertilizedDate = 0,
                        lastRepottedDate = 0,
                        location = "",
                        imageUrl = null
                    ),
                    Plant(
                        id = 2,
                        specieId = 2,
                        name = "Samambaia",
                        speciesFamily = "Polypodiopsida",
                        wateringIntervalDays = 3,
                        lastWateredDate = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(2),
                        fertilizingIntervalDays = 0,
                        reportingIntervalMonths = 0,
                        lastFertilizedDate = 0,
                        lastRepottedDate = 0,
                        location = "",
                        imageUrl = null
                    ),
                    Plant(
                        id = 3,
                        specieId = 3,
                        name = "Cacto",
                        speciesFamily =  "Cactaceae",
                        wateringIntervalDays = 14,
                        lastWateredDate = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(13),
                        fertilizingIntervalDays = 0,
                        reportingIntervalMonths = 0,
                        lastFertilizedDate = 0,
                        lastRepottedDate = 0,
                        location = "",
                        imageUrl = null
                    )
                ),
                errorMessage = null
            ),
            HomeUiState(isLoading = true),
            HomeUiState(errorMessage = "Aconteceu um erro")
        )
}