package com.example.mobilecinema.domain.use_case.genre

import com.example.mobilecinema.data.model.kinopoisk_api.Film
import com.example.mobilecinema.data.model.movie.GenreModel
import com.example.mobilecinema.data.repository.GenreRepositoryImpl
import com.example.mobilecinema.domain.UseCase
import com.example.mobilecinema.domain.repository.GenreRepository
import com.example.mobilecinema.domain.repository.MoviesRepository
import com.example.mobilecinema.domain.use_case.genre.AddGenreUseCase.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class DeleteGenreUseCase(
    private val genreRepository: GenreRepository = GenreRepositoryImpl(),
    configuration: Configuration = Configuration(Dispatchers.IO),
) : UseCase<DeleteGenreUseCase.Request, DeleteGenreUseCase.Response>(configuration) {

    override fun process(request: Request): Flow<Response> =
        flow {
            emit(
                Response(
                    genreRepository.deleteGenre(request.genre)
                )
            )
        }

    data class Request(val genre: GenreModel) : UseCase.Request
    data class Response(val answer: Unit) : UseCase.Response

}