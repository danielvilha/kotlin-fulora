package com.danielvilha.fulora.ui.create

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.danielvilha.fulora.R
import com.danielvilha.fulora.data.Plant
import com.danielvilha.fulora.data.remote.SpeciesListResponse
import com.danielvilha.fulora.ui.preview.ExcludeFromJacocoGeneratedReport
import com.danielvilha.fulora.ui.preview.LightDarkPreview
import com.danielvilha.fulora.ui.theme.FuloraTheme
import com.danielvilha.fulora.util.ErrorText
import com.danielvilha.fulora.util.ProgressIndicator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Composable
@LightDarkPreview
@ExcludeFromJacocoGeneratedReport
private fun ScreenPreview(
    @PreviewParameter(CreatePlantScreenPreview::class)
    value: CreatePlantUiState,
) {
    FuloraTheme {
        CreatePlantScreen(
            state = value,
            onEvent = {},
            events = flow {},
            onBackClick = {},
            onSaveSuccess = {},
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePlantScreen(
    state: CreatePlantUiState,
    onEvent: (CreatePlantUiEvent) -> Unit,
    events: Flow<CreatePlantEvent>,
    onBackClick: () -> Unit,
    onSaveSuccess: () -> Unit,
) {
    LaunchedEffect(key1 = events) {
        events.collect { event ->
            when (event) {
                is CreatePlantEvent.SaveSuccess -> onSaveSuccess()
            }
        }
    }

    Scaffold(
        topBar = {
            PlantSearchBar(
                query = state.searchQuery,
                onQueryChange = { onEvent(CreatePlantUiEvent.OnSearchQueryChange(it)) },
                onSearch = { onEvent(CreatePlantUiEvent.OnSearchClicked(it)) },
                onSpeciesSelected = { onEvent(CreatePlantUiEvent.OnSpeciesSelectedChange(it)) },
                onBackClick = onBackClick,
                searchResults = state.searchResults,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp)
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            when {
                state.isLoading -> ProgressIndicator()
                state.errorMessage != null -> ErrorText(
                    error = state.errorMessage,
                    onRetry = { onEvent(CreatePlantUiEvent.OnRetryClicked) }
                )
                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        AsyncImage(
                            model = state.plant?.imageUrl,
                            contentDescription = "Plant image: ${state.plant?.name}",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    shape = MaterialTheme.shapes.medium
                                )
                                .clip(MaterialTheme.shapes.medium),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Text("Detalhes da Planta", style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = state.plant?.name ?: "",
                            onValueChange = { onEvent(CreatePlantUiEvent.OnPlantNameChange(it)) },
                            label = { Text("Nome da Planta") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        var isLocationMenuExpanded by rememberSaveable { mutableStateOf(false) }
                        val locations = stringArrayResource(id = R.array.localization)

                        ExposedDropdownMenuBox(
                            expanded = isLocationMenuExpanded,
                            onExpandedChange = { isLocationMenuExpanded = it },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = state.plant?.location ?: "",
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Localization") },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(
                                        expanded = isLocationMenuExpanded
                                    )
                                },
                                modifier = Modifier
                                    .menuAnchor(
                                        ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                                        true
                                    )
                                    .fillMaxWidth()
                            )

                            ExposedDropdownMenu(
                                expanded = isLocationMenuExpanded,
                                onDismissRequest = { isLocationMenuExpanded = false }
                            ) {
                                locations.forEach { location ->
                                    DropdownMenuItem(
                                        text = { Text(location) },
                                        onClick = {
                                            onEvent(CreatePlantUiEvent.OnPlantLocationChange(location))
                                            isLocationMenuExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        WateringFrequencyField(state, onEvent)

                        Spacer(modifier = Modifier.height(8.dp))
                        FertilizingFrequencyField(state = state, onEvent = onEvent)

                        Spacer(modifier = Modifier.height(8.dp))
                        ReportingFrequencyField(state = state, onEvent = onEvent)

                        Spacer(modifier = Modifier.weight(1f))

                        Button(
                            onClick = { onEvent(CreatePlantUiEvent.OnSaveClicked) },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = state.isEnabled
                        ) {
                            Text("SALVAR PLANTA")
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlantSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onSpeciesSelected: (String) -> Unit,
    onBackClick: () -> Unit,
    searchResults: SpeciesListResponse?,
    modifier: Modifier = Modifier
) {
    var active by rememberSaveable { mutableStateOf(false) }

    Row {
        SearchBar(
            modifier = modifier
                .fillMaxWidth()
                .semantics { isTraversalGroup = true },
            inputField = @Composable {
                SearchBarDefaults.InputField(
                    query = query,
                    onQueryChange = {
                        onQueryChange(it)
                        if (!active) active = true
                    },
                    onSearch = {
                        onSearch(query)
                        active = false
                    },
                    expanded = active,
                    onExpandedChange = { active = it },
                    placeholder = { Text("Search specie...") },
                    trailingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    leadingIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    },
                )
            },
            expanded = active,
            onExpandedChange = { active = it },
        ) {
            LazyColumn {
                items(searchResults?.species ?: emptyList()) { species ->
                    ListItem(
                        headlineContent = { Text(species.commonName) },
                        modifier = Modifier
                            .clickable {
                                onSpeciesSelected(species.commonName)
                                active = false
                            }
                            .fillMaxWidth()
                    )
                }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WateringFrequencyField(
    state: CreatePlantUiState,
    onEvent: (CreatePlantUiEvent) -> Unit
) {
    var isLocationMenuExpanded by rememberSaveable { mutableStateOf(false) }
    val wateringIntervalDays = stringArrayResource(id = R.array.watering_frequency)

    Column {
        ExposedDropdownMenuBox(
            expanded = isLocationMenuExpanded,
            onExpandedChange = { isLocationMenuExpanded = it },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = state.plant?.wateringIntervalDays?.takeIf { it > 0 }?.toString() ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Watering Frequency (days)") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = isLocationMenuExpanded
                    )
                },
                modifier = Modifier
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, true)
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = isLocationMenuExpanded,
                onDismissRequest = { isLocationMenuExpanded = false }
            ) {
                wateringIntervalDays.forEach { interval ->
                    DropdownMenuItem(
                        text = { Text(interval) },
                        onClick = {
                            onEvent(CreatePlantUiEvent.OnWateringFrequencyChange(interval))
                            isLocationMenuExpanded = false
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FertilizingFrequencyField(
    state: CreatePlantUiState,
    onEvent: (CreatePlantUiEvent) -> Unit
) {
    var isLocationMenuExpanded by rememberSaveable { mutableStateOf(false) }
    val fertilizingIntervalDays = stringArrayResource(id = R.array.fertilizing_frequency)

    Column {
        ExposedDropdownMenuBox(
            expanded = isLocationMenuExpanded,
            onExpandedChange = { isLocationMenuExpanded = it },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = state.plant?.fertilizingIntervalDays?.takeIf { it > 0 }?.toString() ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Fertilization Frequency (days)") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = isLocationMenuExpanded
                    )
                },
                modifier = Modifier
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, true)
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = isLocationMenuExpanded,
                onDismissRequest = { isLocationMenuExpanded = false }
            ) {
                fertilizingIntervalDays.forEach { interval ->
                    DropdownMenuItem(
                        text = { Text(interval) },
                        onClick = {
                            onEvent(CreatePlantUiEvent.OnFertilizingFrequencyChange(interval))
                            isLocationMenuExpanded = false
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReportingFrequencyField(
    state: CreatePlantUiState,
    onEvent: (CreatePlantUiEvent) -> Unit
) {
    var isLocationMenuExpanded by rememberSaveable { mutableStateOf(false) }
    val reportingIntervalMonths = stringArrayResource(id = R.array.reporting_frequency)

    Column {
        ExposedDropdownMenuBox(
            expanded = isLocationMenuExpanded,
            onExpandedChange = { isLocationMenuExpanded = it },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = state.plant?.reportingIntervalMonths?.takeIf { it > 0 }?.toString() ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Frequency of pot replacement (months)") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = isLocationMenuExpanded
                    )
                },
                modifier = Modifier
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, true)
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = isLocationMenuExpanded,
                onDismissRequest = { isLocationMenuExpanded = false }
            ) {
                reportingIntervalMonths.forEach { interval ->
                    DropdownMenuItem(
                        text = { Text(interval) },
                        onClick = {
                            onEvent(CreatePlantUiEvent.OnReportingFrequencyChange(interval))
                            isLocationMenuExpanded = false
                        }
                    )
                }
            }
        }
    }
}

@ExcludeFromJacocoGeneratedReport
class CreatePlantScreenPreview : PreviewParameterProvider<CreatePlantUiState> {
    override val values: Sequence<CreatePlantUiState>
        get() = sequenceOf(
            CreatePlantUiState(
                isLoading = false,
                errorMessage = null,
                plant = Plant(
                    id = 1,
                    name = "Monstera",
                    specieId = 1,
                    speciesFamily = "Araceae",
                    location = "Sala de estar",
                    wateringIntervalDays = 7,
                    fertilizingIntervalDays = 30,
                    reportingIntervalMonths = 12,
                    lastWateredDate = 0,
                    lastFertilizedDate = 0,
                    lastRepottedDate = 0,
                    imageUrl = null
                ),
                searchQuery = "",
                isSearching = false,
                searchResults = null,
                isEnabled = true
            )
        )
}