package com.example.mobilecinema.data.datasource.local.data_source

import com.example.mobilecinema.data.model.movie.ShortMovieModel
import kotlinx.coroutines.flow.Flow

interface FilmLocalDataSource {

    suspend fun addFilm(filmModel: ShortMovieModel)

    suspend fun deleteFilm(filmModel: ShortMovieModel)

    fun getFilm(): Flow<List<ShortMovieModel>>

    suspend fun deleteAll()
}