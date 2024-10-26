package com.example.mobilecinema.data.repository

import com.example.mobilecinema.data.datasource.remote.data_source.MoviesRemoteDataSource
import com.example.mobilecinema.data.model.movie.MovieDetailsModel
import com.example.mobilecinema.data.model.movie.MoviesPagedListModel
import com.example.mobilecinema.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class MoviesRepositoryImpl(private val moviesRemoteDataSource: MoviesRemoteDataSource):MoviesRepository {
    override fun getMoviesPage(number: Int): Flow<MoviesPagedListModel> {
        return moviesRemoteDataSource.getMoviesPage(number)
    }

    override fun getDetails(id: UUID): Flow<MovieDetailsModel> {
        return moviesRemoteDataSource.getDetails(id)
    }
}