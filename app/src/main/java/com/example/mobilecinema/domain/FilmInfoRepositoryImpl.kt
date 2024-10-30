package com.example.mobilecinema.domain

import com.example.mobilecinema.data.model.movie.MovieElementModel
import com.example.mobilecinema.domain.repository.FilmInfoRepository

class FilmInfoRepositoryImpl(private val favoritesMoviesFilmRating: MoviesFilmRating) :
    FilmInfoRepository {

    override fun getFilmRating(movies: List<MovieElementModel>): List<Float> =
        favoritesMoviesFilmRating.getFilmRating(movies)

}