package com.danielvilha.fulora.service

import com.danielvilha.fulora.data.remote.SpeciesListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("species-list")
    suspend fun searchSpecies(
        @Query("q") query: String
    ): SpeciesListResponse
}