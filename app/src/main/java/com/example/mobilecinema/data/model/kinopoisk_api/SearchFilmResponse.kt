package com.example.mobilecinema.data.model.kinopoisk_api

import kotlinx.serialization.Serializable

@Serializable
data class SearchFilmResponse (
    val keyword: String?,
    val pagesCount: Int?,
    val searchFilmsCountResult: Int?,
    val films: List<SearchFilmDto>,
)