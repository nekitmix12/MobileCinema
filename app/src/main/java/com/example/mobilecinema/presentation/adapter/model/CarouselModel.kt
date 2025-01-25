package com.example.mobilecinema.presentation.adapter.model

import android.graphics.Bitmap
import com.example.mobilecinema.data.model.movie.GenreModel
import com.example.mobilecinema.presentation.adapter.Item

data class CarouselModel(
    val id: String,
    val name: String?,
    val poster: Bitmap?,
    var genres: List<Pair<GenreModel, Boolean>>,
) : Item
