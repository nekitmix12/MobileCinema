package com.example.mobilecinema.data.datasource.remote.api_service

import com.example.mobilecinema.data.model.kinopoisk_api.Film
import com.example.mobilecinema.data.model.kinopoisk_api.SearchFilmResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServiceKinopoisk {
    @GET("/api/v2.2/films/{id}")
    suspend fun getFilmById(@Path("id") id: Int): Film

    /*    @GET("/api/v2.2/film/{id}/box_office")
        fun getBoxOffice(@Path("id")id:String):BoxOfficeResponse*/

    @GET("/api/v2.1/films/search-by-keyword")
    suspend fun searchFilms(
        @Query("keyword") keyword: String,
        @Query("page") page: Int,
    ): SearchFilmResponse
}