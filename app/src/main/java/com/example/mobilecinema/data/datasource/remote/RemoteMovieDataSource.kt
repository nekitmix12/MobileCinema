package com.example.mobilecinema.data.datasource.remote

import com.example.mobilecinema.data.model.movie.MovieDetailsModel
import com.example.mobilecinema.data.model.movie.MoviesPagedListModel
import retrofit2.http.GET
import retrofit2.http.Path

interface RemoteMovieDataSource {
    @GET("/movies/{page}")
    suspend fun getMovies(@Path("page") page: Int): MoviesPagedListModel

    @GET("/movies/details/{id}")
    suspend fun getFilmDetails(@Path("id") id: Int): MovieDetailsModel
}