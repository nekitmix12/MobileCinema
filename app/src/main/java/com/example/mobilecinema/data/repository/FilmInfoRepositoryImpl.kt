package com.example.mobilecinema.data.repository

import com.example.mobilecinema.data.model.movie.MovieElementModel
import com.example.mobilecinema.domain.MoviesFilmRating
import com.example.mobilecinema.domain.repository.FilmInfoRepository

class FilmInfoRepositoryImpl(private val favoritesMoviesFilmRating: MoviesFilmRating) :
    FilmInfoRepository {

    override fun getFilmRating(movies: List<MovieElementModel>): List<Float> =
        favoritesMoviesFilmRating.getFilmRating(movies)

}