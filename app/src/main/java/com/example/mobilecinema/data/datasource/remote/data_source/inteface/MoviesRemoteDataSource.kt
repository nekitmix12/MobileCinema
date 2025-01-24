package com.example.mobilecinema.data.datasource.remote.data_source.inteface

import com.example.mobilecinema.data.model.movie.MovieDetailsModel
import com.example.mobilecinema.data.model.movie.MoviesPagedListModel
import kotlinx.coroutines.flow.Flow

interface MoviesRemoteDataSource {
    fun getMoviesPage(number: Int): Flow<MoviesPagedListModel>

    fun getDetails(id: String): Flow<MovieDetailsModel>
}