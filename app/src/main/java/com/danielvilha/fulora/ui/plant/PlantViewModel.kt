package com.danielvilha.fulora.ui.plant

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danielvilha.fulora.data.Plant
import com.danielvilha.fulora.data.PlantDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlantViewModel @Inject constructor(
    private val plantDao: PlantDao,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val plantId: Int = checkNotNull(savedStateHandle["plantId"])

    val uiState: StateFlow<PlantUiState> =
        plantDao.getById(plantId)
            .map { plant -> PlantUiState(plant = plant) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = PlantUiState(
                    plant = Plant.create(),
                    isLoading = true
                )
            )

    fun onWaterPlant() {
        viewModelScope.launch {
            uiState.first().plant.let {
                plantDao.update(it.copy(lastWateredDate = System.currentTimeMillis()))
            }
        }
    }

    fun onFertilizePlant() {
        viewModelScope.launch {
            uiState.first().plant.let {
                plantDao.update(it.copy(lastFertilizedDate = System.currentTimeMillis()))
            }
        }
    }

    fun onRepotPlant() {
        viewModelScope.launch {
            uiState.first().plant.let {
                plantDao.update(it.copy(lastRepottedDate = System.currentTimeMillis()))
            }
        }
    }
}
