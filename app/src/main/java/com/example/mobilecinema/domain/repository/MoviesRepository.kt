package com.example.mobilecinema.domain.repository

import com.example.mobilecinema.data.model.movie.MovieDetailsModel
import com.example.mobilecinema.data.model.movie.MoviesPagedListModel
import com.example.mobilecinema.data.model.movie.ShortMovieModel
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {
    fun getMoviesPage(number: Int): Flow<MoviesPagedListModel>

    fun getDetails(id: String): Flow<MovieDetailsModel>

    suspend fun addFilm(filmModel: ShortMovieModel)

    suspend fun deleteFilm(filmModel: ShortMovieModel)

    fun getFilms(): Flow<List<ShortMovieModel>>

    suspend fun deleteAll()
}