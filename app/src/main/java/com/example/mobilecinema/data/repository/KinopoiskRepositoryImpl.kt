package com.example.mobilecinema.data.repository

import com.example.mobilecinema.data.datasource.remote.data_source.KinopoiskRemoteDataSource
import com.example.mobilecinema.data.model.kinopoisk_api.Film
import com.example.mobilecinema.data.model.kinopoisk_api.SearchFilmDto
import com.example.mobilecinema.data.model.kinopoisk_api.SearchFilmResponse
import com.example.mobilecinema.domain.repository.KinopoiskRepository
import kotlinx.coroutines.flow.Flow

class KinopoiskRepositoryImpl(
    private val kinopoiskRemoteDataSource: KinopoiskRemoteDataSource
) :KinopoiskRepository{
    override fun getFilmById(id:Int): Flow<Film> =
        kinopoiskRemoteDataSource.getFilmById(id)

    override fun searchFilms(keyword:String, page:Int): Flow<SearchFilmResponse> =
        kinopoiskRemoteDataSource.searchFilms(keyword,page)
}