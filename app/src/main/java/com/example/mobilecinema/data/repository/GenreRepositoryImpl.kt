package com.example.mobilecinema.data.repository

import com.example.mobilecinema.data.datasource.local.data_source.GenreLocalDataSource
import com.example.mobilecinema.data.model.movie.GenreModel
import com.example.mobilecinema.domain.repository.GenreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GenreRepositoryImpl(
    private val genreLocalDataSource: GenreLocalDataSource
):GenreRepository {
    override suspend fun addGenres(genreModel: GenreModel) {
        genreLocalDataSource.addGenres(genreModel)
    }

    override suspend fun deleteGenre(genreModel: GenreModel) {
        genreLocalDataSource.deleteGenre(genreModel)
    }

    override fun getGenres(): Flow<List<GenreModel>> {
        return genreLocalDataSource.getGenres()
    }

    override suspend fun deleteAll() {
        genreLocalDataSource.deleteAll()
    }
}