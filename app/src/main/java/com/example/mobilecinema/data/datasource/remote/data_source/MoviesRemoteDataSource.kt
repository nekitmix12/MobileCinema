package com.example.mobilecinema.data.datasource.remote.data_source

import com.example.mobilecinema.data.model.favorite_movies.MoviesListModel
import com.example.mobilecinema.data.model.movie.MovieDetailsModel
import com.example.mobilecinema.data.model.movie.MoviesPagedListModel
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface MoviesRemoteDataSource {
    fun getMoviesPage(number: Int): Flow<MoviesPagedListModel>

    fun getDetails(id: UUID): Flow<MovieDetailsModel>
}