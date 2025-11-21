package com.danielvilha.fulora.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Plant(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var specieId: Int?,
    var name: String = "",
    var speciesFamily: String?,
    var location: String = "",
    var wateringIntervalDays: Int = 0,
    var fertilizingIntervalDays: Int = 0,
    var reportingIntervalMonths: Int = 0,
    var lastWateredDate: Long = 0L,
    var lastFertilizedDate: Long = 0L,
    var lastRepottedDate: Long = 0L,
    var imageUrl: String?,
) {
    companion object {
        fun create() = Plant(
            specieId = null,
            name = "",
            speciesFamily = null,
            location = "",
            wateringIntervalDays = 0,
            fertilizingIntervalDays = 0,
            reportingIntervalMonths = 0,
            lastWateredDate = 0L,
            lastFertilizedDate = 0L,
            lastRepottedDate = 0L,
            imageUrl = null,
        )
    }
}
