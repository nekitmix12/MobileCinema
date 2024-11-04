package com.example.mobilecinema.domain.repository

import com.example.mobilecinema.data.model.movie.MovieDetailsModel
import com.example.mobilecinema.data.model.movie.MoviesPagedListModel
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface MoviesRepository {
    fun getMoviesPage(number: Int): Flow<MoviesPagedListModel>

    fun getDetails(id: String): Flow<MovieDetailsModel>
}