package com.danielvilha.fulora.data.remote

import com.google.gson.annotations.SerializedName

data class SpeciesDto(
    val id: Int,
    @SerializedName("common_name")
    val commonName: String,
    val watering: String,
    @SerializedName("scientific_name")
    val scientificName: List<String>,
    @SerializedName("other_name")
    val otherName: List<String>,
    val family: String,
    val hybrid: String?,
    val authority: String?,
    val subspecies: String?,
    val cultivar: String?,
    val variety: String?,
    @SerializedName("species_epithet")
    val speciesEpithet: String,
    val genus: String,
    @SerializedName("default_image")
    val defaultImage: DefaultImageDto
)
