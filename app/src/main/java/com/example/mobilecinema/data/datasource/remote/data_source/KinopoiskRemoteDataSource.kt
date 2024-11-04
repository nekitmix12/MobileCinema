package com.example.mobilecinema.data.datasource.remote.data_source

import com.example.mobilecinema.data.model.kinopoisk_api.Film
import com.example.mobilecinema.data.model.kinopoisk_api.SearchFilmDto
import com.example.mobilecinema.data.model.kinopoisk_api.SearchFilmResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Body
import retrofit2.http.Path

interface KinopoiskRemoteDataSource {

    fun getFilmById(id:Int): Flow<Film>

    fun searchFilms(keyword:String, page:Int): Flow<SearchFilmResponse>
}