package com.example.mobilecinema.domain.use_case.kinopoisk_use_case

import com.example.mobilecinema.data.model.kinopoisk_api.Film
import com.example.mobilecinema.data.repository.KinopoiskRepositoryImpl
import com.example.mobilecinema.domain.UseCase
import com.example.mobilecinema.domain.repository.KinopoiskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class GetFilmUseCase(
    private val kinopoiskRepository: KinopoiskRepository = KinopoiskRepositoryImpl(),
    configuration: Configuration = Configuration(Dispatchers.IO),
) : UseCase<GetFilmUseCase.Request,
        GetFilmUseCase.Response>(configuration) {

    override fun process(request: Request): Flow<Response> =
            kinopoiskRepository.getFilmById(request.id)
                .map {
                    Response(it)
                }

    data class Request(val id: Int) : UseCase.Request
    data class Response(val answer: Film) : UseCase.Response

}