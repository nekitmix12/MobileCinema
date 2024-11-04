package com.example.mobilecinema.data.model.kinopoisk_api

import kotlinx.serialization.Serializable

@Serializable
data class SearchFilmDto(
    val filmId:Int,
    val nameRu:String,
    val nameEn:String,
    val type:String,
    val year:Int,
    val description:String,
    val filmLength:String,
    val countries: List<CountiesDto>,
    val genres: List<GenreDto>,
    val rating:String,
    val ratingVoteCount:Int,
    val posterUrl:String,
    val posterUrlPreview:String,
)