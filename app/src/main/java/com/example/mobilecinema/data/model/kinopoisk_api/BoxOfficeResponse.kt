package com.example.mobilecinema.data.model.kinopoisk_api

import kotlinx.serialization.Serializable

@Serializable
data class BoxOfficeResponse(
    val total:Int,
    val items:List<BoxOfficeDto>,
)
