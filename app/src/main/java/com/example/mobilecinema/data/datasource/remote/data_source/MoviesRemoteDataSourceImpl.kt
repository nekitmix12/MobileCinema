package com.example.mobilecinema.data.datasource.remote.data_source

import com.example.mobilecinema.data.datasource.remote.api_service.ApiServiceMovie
import com.example.mobilecinema.data.model.movie.MovieDetailsModel
import com.example.mobilecinema.data.model.movie.MoviesPagedListModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class MoviesRemoteDataSourceImpl(private val apiServiceMovie: ApiServiceMovie) :
    MoviesRemoteDataSource {
    override fun getMoviesPage(number: Int): Flow<MoviesPagedListModel> {
        return flow {
            emit(apiServiceMovie.getMovies(number))
        }.map {
            MoviesPagedListModel(
                it.movies,
                it.pageInfo
            )
        }
    }

    override fun getDetails(id: String): Flow<MovieDetailsModel> {
        return flow {
            emit(apiServiceMovie.getFilmDetails(id))
        }.map {
            MovieDetailsModel(
                it.id,
                it.name,
                it.poster,
                it.year,
                it.country,
                it.genres,
                it.reviews,
                it.filmTime,
                it.tagline,
                it.description,
                it.director,
                it.budget,
                it.fees,
                it.ageLimit,
            )
        }
    }
}