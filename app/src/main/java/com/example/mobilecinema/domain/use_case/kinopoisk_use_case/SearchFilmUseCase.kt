package com.example.mobilecinema.domain.use_case.kinopoisk_use_case

import com.example.mobilecinema.data.datasource.remote.data_source.implementation.KinopoiskRemoteDataSourceImpl
import com.example.mobilecinema.data.model.kinopoisk_api.SearchFilmResponse
import com.example.mobilecinema.domain.UseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchFilmUseCase(
    private val kinopoiskRepository: KinopoiskRemoteDataSourceImpl = KinopoiskRemoteDataSourceImpl(),
    configuration: Configuration = Configuration(Dispatchers.IO),
) : UseCase<SearchFilmUseCase.Request, SearchFilmUseCase.Response>(configuration) {

    override fun process(request: Request): Flow<Response> =
        kinopoiskRepository.searchFilms(request.id, request.page)
            .map {
                Response(it)
            }


    data class Request(val id: String, val page: Int) : UseCase.Request
    data class Response(val answer: SearchFilmResponse) : UseCase.Response

}