package com.example.mobilecinema.data.model.favorite_movies

import com.example.mobilecinema.data.model.movie.MovieElementModel
import kotlinx.serialization.Serializable

@Serializable
data class MoviesListModel (
    val movies: List<MovieElementModel>?
)