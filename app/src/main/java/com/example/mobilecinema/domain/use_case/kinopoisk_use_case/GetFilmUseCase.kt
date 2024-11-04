package com.example.mobilecinema.domain.use_case.kinopoisk_use_case

import com.example.mobilecinema.data.model.kinopoisk_api.Film
import com.example.mobilecinema.domain.UseCase
import com.example.mobilecinema.domain.repository.KinopoiskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class GetFilmUseCase(
    private val kinopoiskRepository: KinopoiskRepository,
    configuration: Configuration,
) : UseCase<GetFilmUseCase.Request,
        GetFilmUseCase.Response>(configuration) {

    override fun process(request: Request): Flow<Response> =
        flow {
            kinopoiskRepository.getFilmById(request.id)
                .map {
                    Response(it)
                }
        }

    data class Request(val id: Int) : UseCase.Request
    data class Response(val answer: Film) : UseCase.Response

}