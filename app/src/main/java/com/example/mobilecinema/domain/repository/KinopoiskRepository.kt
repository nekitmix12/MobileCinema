package com.example.mobilecinema.domain.repository

import com.example.mobilecinema.data.model.kinopoisk_api.Film
import com.example.mobilecinema.data.model.kinopoisk_api.SearchFilmResponse
import kotlinx.coroutines.flow.Flow

interface KinopoiskRepository {
    fun getFilmById(id: Int): Flow<Film>

    fun searchFilms(keyword: String, page: Int): Flow<SearchFilmResponse>
}