package com.example.mobilecinema.domain

import com.example.mobilecinema.data.model.movie.MovieElementModel

interface MoviesFilmRating {
    fun getFilmRating(movies: List<MovieElementModel>): List<Float>
}