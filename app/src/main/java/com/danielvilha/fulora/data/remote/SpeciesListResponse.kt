package com.danielvilha.fulora.data.remote

import com.google.gson.annotations.SerializedName

data class SpeciesListResponse(
    @SerializedName("data")
    val species: List<SpeciesDto>
)
