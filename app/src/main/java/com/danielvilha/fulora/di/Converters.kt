package com.danielvilha.fulora.di

import androidx.room.TypeConverter
import com.danielvilha.fulora.data.remote.SpeciesDto
import com.google.gson.Gson

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromSpeciesDto(species: SpeciesDto?): String? {
        return gson.toJson(species)
    }

    @TypeConverter
    fun toSpeciesDto(speciesJson: String?): SpeciesDto? {
        return gson.fromJson(speciesJson, SpeciesDto::class.java)
    }
}