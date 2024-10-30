package com.example.mobilecinema.domain

import com.example.mobilecinema.data.model.movie.MovieElementModel

class MoviesFilmRatingImpl : MoviesFilmRating {
    override fun getFilmRating(movies: List<MovieElementModel>): List<Float> =
        movies.map {
            var rating = 0f
            it.reviews.forEach { itRating ->
                rating += itRating?.rating ?: 0
            }
            val averageRating = if (it.reviews.isNotEmpty()) {
                rating / it.reviews.size
            } else {
                0f
            }
            averageRating
        }

}