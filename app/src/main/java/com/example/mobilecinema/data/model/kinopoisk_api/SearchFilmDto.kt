package com.example.mobilecinema.data.model.kinopoisk_api

import androidx.annotation.Nullable
import kotlinx.serialization.Serializable


@Serializable
data class SearchFilmDto(
    val filmId: Int,
    val nameRu: String? = null,
    val nameEn: String? = null,
    val type: Type? = null,
    val year: String? = null,
    val description: String? = null,
    val filmLength: String? = null,
    val countries: List<Country> = emptyList(),
    val genres: List<Genre> = emptyList(),
    val rating: String? = null,
    val ratingVoteCount: Int? = null,
    val posterUrl: String? = null,
    val posterUrlPreview: String? = null
)