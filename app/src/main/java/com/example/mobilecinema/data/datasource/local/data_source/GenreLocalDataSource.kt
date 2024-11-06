package com.example.mobilecinema.data.datasource.local.data_source

import com.example.mobilecinema.data.model.movie.GenreModel
import kotlinx.coroutines.flow.Flow

interface GenreLocalDataSource {

    suspend fun addGenres(genreModel: GenreModel)

    suspend fun deleteGenre(genreModel: GenreModel)

    fun getGenres(): Flow<List<GenreModel>>

    suspend fun deleteAll()
}