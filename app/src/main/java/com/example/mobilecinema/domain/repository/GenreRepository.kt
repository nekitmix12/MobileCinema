package com.example.mobilecinema.domain.repository

import com.example.mobilecinema.data.model.movie.GenreModel
import kotlinx.coroutines.flow.Flow

interface GenreRepository {
    suspend fun addGenres(genreModel: GenreModel)

    suspend fun deleteGenre(genreModel: GenreModel)

    fun getGenres(): Flow<List<GenreModel>>

    suspend fun deleteAll()
}