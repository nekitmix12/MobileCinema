package com.example.mobilecinema.domain.model

import com.example.mobilecinema.data.model.movie.GenreModel

data class MovieFeedCardModel(
    val id: String,
    val name:String,
    val poster:String,
    val year:Int?,
    val genre: List<GenreModel>
)
