package com.example.mobilecinema.data.datasource.remote.data_source.implementation

import android.util.Log
import com.example.mobilecinema.data.datasource.remote.api_service.ApiServiceMovie
import com.example.mobilecinema.data.datasource.remote.data_source.inteface.MoviesRemoteDataSource
import com.example.mobilecinema.data.model.movie.MovieDetailsModel
import com.example.mobilecinema.data.model.movie.MoviesPagedListModel
import com.example.mobilecinema.data.network.AuthInterceptor
import com.example.mobilecinema.data.network.NetworkModule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class MoviesRemoteDataSourceImpl(
    private val apiServiceMovie: ApiServiceMovie = NetworkModule().provideMovieService(
        NetworkModule().provideRetrofit(
            NetworkModule().provideOkHttpClient(
                AuthInterceptor()
            )
        )
    ),
) : MoviesRemoteDataSource {
    override fun getMoviesPage(number: Int): Flow<MoviesPagedListModel> {
        return flow {
            emit(apiServiceMovie.getMovies(number))
        }.map {
            MoviesPagedListModel(
                it.movies, it.pageInfo
            )
        }
    }

    override fun getDetails(id: String): Flow<MovieDetailsModel> {
        return flow {
            Log.d("intercept in rep", "start")
            emit(apiServiceMovie.getFilmDetails(id))
        }.map {
            if (it.body() != null) {
                Log.d("intercept in rep", it.body().toString())
                it.body()!!
            }
            else
                throw IllegalArgumentException(it.errorBody().toString())
        }
    }
}