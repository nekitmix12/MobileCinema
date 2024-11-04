package com.example.mobilecinema.data.model.kinopoisk_api

import kotlinx.serialization.Serializable

@Serializable
data class BoxOfficeDto (
    val type:String,
    val amount:Int,
    val currentCode:String,
    val name:String,
    val symbol:String,
)