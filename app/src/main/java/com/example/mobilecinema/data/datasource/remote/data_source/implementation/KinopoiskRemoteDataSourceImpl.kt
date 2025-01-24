package com.example.mobilecinema.data.datasource.remote.data_source.implementation

import android.util.Log
import com.example.mobilecinema.data.datasource.remote.api_service.ApiServiceKinopoisk
import com.example.mobilecinema.data.datasource.remote.data_source.inteface.KinopoiskRemoteDataSource
import com.example.mobilecinema.data.model.kinopoisk_api.Film
import com.example.mobilecinema.data.model.kinopoisk_api.SearchFilmResponse
import com.example.mobilecinema.data.network.NetworkModule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class KinopoiskRemoteDataSourceImpl(
    private val apiServiceKinopoisk: ApiServiceKinopoisk = NetworkModule().providerKinopoiskService(
        NetworkModule().provideRetrofitForKinopoisk(
            NetworkModule().provideOkHttpClientWithKinopoiskInterceptor()
        )
    ),
) : KinopoiskRemoteDataSource {

    override fun getFilmById(id: Int): Flow<Film> = flow {
            emit(apiServiceKinopoisk.getFilmById(id))
    }.map {
        it
    }

    override fun searchFilms(keyword: String, page: Int): Flow<SearchFilmResponse> = flow {
        emit(apiServiceKinopoisk.searchFilms(keyword, page))
    }.map {
        SearchFilmResponse(
            it.keyword,
            it.pagesCount,
            it.searchFilmsCountResult,
            it.films
        )

    }

}