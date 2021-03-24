package com.example.echo_kt.api

import com.google.gson.annotations.SerializedName

data class MusicSearchResponse (
    @field:SerializedName("results") val results: List<Info>,
    @field:SerializedName("total_pages") val totalPages: Int
)
