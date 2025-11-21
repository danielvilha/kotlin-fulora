package com.danielvilha.fulora.ui.plant

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.danielvilha.fulora.data.Plant
import com.danielvilha.fulora.ui.preview.ExcludeFromJacocoGeneratedReport
import com.danielvilha.fulora.ui.preview.LightDarkPreview
import com.danielvilha.fulora.ui.theme.FuloraTheme
import com.danielvilha.fulora.util.ProgressIndicator
import java.util.concurrent.TimeUnit

@Composable
@LightDarkPreview
@ExcludeFromJacocoGeneratedReport
private fun ScreenPreview(
    @PreviewParameter(PlantScreenPreview::class)
    state: PlantUiState
) {
    FuloraTheme {
        PlantScreen(
            state = state,
            onBackClick = {},
            onWaterPlant = {},
            onFertilizePlant = {},
            onRepotPlant = {},
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantScreen(
    state: PlantUiState,
    onBackClick: () -> Unit,
    onWaterPlant: () -> Unit,
    onFertilizePlant: () -> Unit,
    onRepotPlant: () -> Unit,
) {
    val plant = state.plant

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = plant.name)
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            when {
                state.isLoading -> ProgressIndicator()
                else -> {
                    PlantContent(
                        plant = plant,
                        onWaterPlant = onWaterPlant,
                        onFertilizePlant = onFertilizePlant,
                        onRepotPlant = onRepotPlant,
                    )
                }
            }
        }
    }
}

@Composable
private fun PlantContent(
    plant: Plant,
    onWaterPlant: () -> Unit,
    onFertilizePlant: () -> Unit,
    onRepotPlant: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        AsyncImage(
            model = plant.imageUrl,
            contentDescription = "Plant image: ${plant.name}",
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp).background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = MaterialTheme.shapes.medium
                )
                .clip(MaterialTheme.shapes.medium),
            contentScale = ContentScale.Crop
        )
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = plant.speciesFamily ?: "No species family",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (plant.location.isNotEmpty()) {
                Text(
                    text = "Location: ${plant.location}",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            WateringInfo(plant = plant, onWaterPlant = onWaterPlant)
            Spacer(modifier = Modifier.height(16.dp))
            FertilizingInfo(plant = plant, onFertilizePlant = onFertilizePlant)
            Spacer(modifier = Modifier.height(16.dp))
            RepottingInfo(plant = plant, onRepotPlant = onRepotPlant)
        }
    }
}

@Composable
private fun WateringInfo(plant: Plant, onWaterPlant: () -> Unit) {
    val nextWateringDate = plant.lastWateredDate + TimeUnit.DAYS.toMillis(plant.wateringIntervalDays.toLong())
    val remainingDays = TimeUnit.MILLISECONDS.toDays(nextWateringDate - System.currentTimeMillis())

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Watering", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Interval: Every ${plant.wateringIntervalDays} days")
            Spacer(modifier = Modifier.height(4.dp))
            if (plant.lastWateredDate > 0) {
                val daysAgo = TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - plant.lastWateredDate)
                Text("Last watered: $daysAgo days ago")
            }
            Spacer(modifier = Modifier.height(4.dp))
            val nextWateringText = when {
                remainingDays > 1 -> "Next watering in $remainingDays days"
                remainingDays == 1L -> "Next watering tomorrow"
                remainingDays == 0L -> "Water today"
                else -> "Watering is overdue by ${-remainingDays} days"
            }
            Text(nextWateringText)
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onWaterPlant,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("WATER PLANT")
            }
        }
    }
}

@Composable
private fun FertilizingInfo(plant: Plant, onFertilizePlant: () -> Unit) {
    val nextFertilizingDate = plant.lastFertilizedDate + TimeUnit.DAYS.toMillis(plant.fertilizingIntervalDays.toLong())
    val remainingDays = TimeUnit.MILLISECONDS.toDays(nextFertilizingDate - System.currentTimeMillis())

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Fertilizing", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Interval: Every ${plant.fertilizingIntervalDays} days")
            Spacer(modifier = Modifier.height(4.dp))
            if (plant.lastFertilizedDate > 0) {
                val daysAgo = TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - plant.lastFertilizedDate)
                Text("Last fertilized: $daysAgo days ago")
            }
            Spacer(modifier = Modifier.height(4.dp))
            val nextFertilizingText = when {
                remainingDays > 1 -> "Next fertilizing in $remainingDays days"
                remainingDays == 1L -> "Next fertilizing tomorrow"
                remainingDays == 0L -> "Fertilize today"
                else -> "Fertilizing is overdue by ${-remainingDays} days"
            }
            Text(nextFertilizingText)
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onFertilizePlant,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("FERTILIZE PLANT")
            }
        }
    }
}

@Composable
private fun RepottingInfo(plant: Plant, onRepotPlant: () -> Unit) {
    val nextRepottingDate = plant.lastRepottedDate + TimeUnit.DAYS.toMillis(plant.reportingIntervalMonths.toLong() * 30) // Approximation
    val remainingDays = TimeUnit.MILLISECONDS.toDays(nextRepottingDate - System.currentTimeMillis())

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Repotting", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Interval: Every ${plant.reportingIntervalMonths} months")
            Spacer(modifier = Modifier.height(4.dp))
            if (plant.lastRepottedDate > 0) {
                val daysAgo = TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - plant.lastRepottedDate)
                Text("Last repotted: $daysAgo days ago")
            }
            Spacer(modifier = Modifier.height(4.dp))
            val nextRepottingText = when {
                remainingDays > 1 -> "Next repotting in $remainingDays days"
                remainingDays == 1L -> "Next repotting tomorrow"
                remainingDays == 0L -> "Repot today"
                else -> "Repotting is overdue by ${-remainingDays} days"
            }
            Text(nextRepottingText)
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onRepotPlant,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("REPOT PLANT")
            }
        }
    }
}

@ExcludeFromJacocoGeneratedReport
class PlantScreenPreview  : PreviewParameterProvider<PlantUiState> {
    override val values: Sequence<PlantUiState>
        get() = sequenceOf(
            PlantUiState(
                plant = Plant(
                    id = 1,
                    specieId = 1,
                    name = "Samambaia",
                    speciesFamily = "Polypodiopsida",
                    wateringIntervalDays = 3,
                    lastWateredDate = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(2),
                    fertilizingIntervalDays = 70,
                    reportingIntervalMonths = 140,
                    lastFertilizedDate = 0,
                    lastRepottedDate = 0,
                    location = "Living room",
                    imageUrl = ""
                ),
                isLoading = false
            ),
            PlantUiState(plant = Plant.create(), isLoading = true),
        )
}
