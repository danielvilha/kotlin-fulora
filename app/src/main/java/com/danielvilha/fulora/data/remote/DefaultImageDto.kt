package com.danielvilha.fulora.data.remote

import com.google.gson.annotations.SerializedName

data class DefaultImageDto(
    val license: Int,
    @SerializedName("license_name")
    val licenseName: String,
    @SerializedName("license_url")
    val licenseUrl: String,
    @SerializedName("original_url")
    val originalUrl: String,
    @SerializedName("regular_url")
    val regularUrl: String,
    @SerializedName("medium_url")
    val mediumUrl: String,
    @SerializedName("small_url")
    val smallUrl: String,
    val thumbnail: String
)