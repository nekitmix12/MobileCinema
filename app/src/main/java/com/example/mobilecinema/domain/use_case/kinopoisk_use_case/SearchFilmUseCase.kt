package com.example.mobilecinema.domain.use_case.kinopoisk_use_case

import com.example.mobilecinema.data.model.kinopoisk_api.SearchFilmResponse
import com.example.mobilecinema.domain.UseCase
import com.example.mobilecinema.domain.repository.KinopoiskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class SearchFilmUseCase(
    private val kinopoiskRepository: KinopoiskRepository,
    configuration: Configuration,
) : UseCase<SearchFilmUseCase.Request, SearchFilmUseCase.Response>(configuration) {

    override fun process(request: Request): Flow<Response> =
        flow {
            kinopoiskRepository.searchFilms(request.id, request.page)
                .map {
                    Response(it)
                }
        }

    data class Request(val id: String, val page: Int) : UseCase.Request
    data class Response(val answer: SearchFilmResponse) : UseCase.Response

}