package com.example.mobilecinema.domain.use_case.genre

import com.example.mobilecinema.data.model.kinopoisk_api.Film
import com.example.mobilecinema.domain.UseCase
import com.example.mobilecinema.domain.repository.GenreRepository
import com.example.mobilecinema.domain.repository.MoviesRepository
import com.example.mobilecinema.domain.use_case.genre.AddGenreUseCase.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class DeleteAllGenresUseCase(
    private val genreRepository: GenreRepository,
    configuration: Configuration,
) : UseCase<DeleteAllGenresUseCase.Request, DeleteAllGenresUseCase.Response>(configuration) {

    override fun process(request: Request): Flow<Response> =
        flow {
            emit(
                Response(
                    genreRepository.deleteAll()
                )
            )
        }

    override fun defaultRequest(): Request {
        return Request()
    }
    class Request : UseCase.Request
    data class Response(val answer: Unit) : UseCase.Response

}