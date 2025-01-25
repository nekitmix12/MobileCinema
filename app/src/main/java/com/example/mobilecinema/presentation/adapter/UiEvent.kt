package com.example.mobilecinema.presentation.adapter

import com.example.mobilecinema.data.model.movie.GenreModel

sealed class UiEvent {
    data class GenreClicked(val genre: GenreModel) : UiEvent()
    data class CarouselButtonClicked(val moviesId: String) : UiEvent()
    data object ButtonAllClicked : UiEvent()
}