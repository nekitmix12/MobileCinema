package com.example.mobilecinema.domain

import com.example.mobilecinema.data.model.movie.MovieElementModel
import java.math.BigDecimal
import java.math.RoundingMode

class MoviesFilmRatingImpl : MoviesFilmRating {

    override fun getFilmRating(movies: List<MovieElementModel>): List<Float> =
        movies.map {
            var rating = 0f
            it.reviews.forEach { itRating ->
                rating += itRating?.rating ?: 0
            }
            val averageRating = if (it.reviews.isNotEmpty()) {
                val rawAverage = rating / it.reviews.size
                BigDecimal(rawAverage.toDouble()).setScale(1, RoundingMode.HALF_UP).toFloat()
            } else {
                0f
            }
            averageRating
        }

}