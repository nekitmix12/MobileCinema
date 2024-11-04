package com.example.mobilecinema.data.datasource.remote.data_source

import com.example.mobilecinema.data.datasource.remote.api_service.ApiServiceKinopoisk
import com.example.mobilecinema.data.model.kinopoisk_api.Film
import com.example.mobilecinema.data.model.kinopoisk_api.SearchFilmDto
import com.example.mobilecinema.data.model.kinopoisk_api.SearchFilmResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class KinopoiskRemoteDataSourceImpl(
    private val apiServiceKinopoisk: ApiServiceKinopoisk,
):KinopoiskRemoteDataSource {

    override fun getFilmById(id: Int): Flow<Film>
        = flow{
            emit(apiServiceKinopoisk.getFilmById(id))
    }.map {
        it
    }

    override fun searchFilms(keyword: String, page: Int): Flow<SearchFilmResponse> =
    flow{
        emit(apiServiceKinopoisk.searchFilms(keyword,page))
    }.map {
        it
    }
}