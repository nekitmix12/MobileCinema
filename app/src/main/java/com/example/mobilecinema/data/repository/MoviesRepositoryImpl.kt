package com.example.mobilecinema.data.repository

import com.example.mobilecinema.data.datasource.local.data_source.FilmLocalDataSource
import com.example.mobilecinema.data.datasource.remote.data_source.MoviesRemoteDataSource
import com.example.mobilecinema.data.model.movie.MovieDetailsModel
import com.example.mobilecinema.data.model.movie.MoviesPagedListModel
import com.example.mobilecinema.data.model.movie.ShortMovieModel
import com.example.mobilecinema.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class MoviesRepositoryImpl(
    private val moviesRemoteDataSource: MoviesRemoteDataSource,
    private val filmLocalDataSource: FilmLocalDataSource
):MoviesRepository {
    override fun getMoviesPage(number: Int): Flow<MoviesPagedListModel> {
        return moviesRemoteDataSource.getMoviesPage(number)
    }

    override fun getDetails(id: String): Flow<MovieDetailsModel> {
        return moviesRemoteDataSource.getDetails(id)
    }

    override suspend fun addFilm(filmModel: ShortMovieModel) {
        filmLocalDataSource.addFilm(filmModel)
    }

    override suspend fun deleteFilm(filmModel: ShortMovieModel) {
        filmLocalDataSource.deleteFilm(filmModel)
    }

    override fun getFilms(): Flow<List<ShortMovieModel>> {
        return filmLocalDataSource.getFilm()
    }

    override suspend fun deleteAll() {
        filmLocalDataSource.deleteAll()
    }
}