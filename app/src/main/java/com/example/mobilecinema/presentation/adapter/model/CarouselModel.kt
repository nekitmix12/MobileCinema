package com.example.mobilecinema.presentation.adapter.model

import com.example.mobilecinema.presentation.adapter.Item

data class CarouselModel(
    val id: String,
    val name: String?,
    val poster: String?,
    var genres: List<Pair<String, Boolean>>,
) : Item
