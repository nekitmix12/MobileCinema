package com.example.mobilecinema.domain.use_case.genre

import com.example.mobilecinema.data.model.movie.GenreModel
import com.example.mobilecinema.data.repository.GenreRepositoryImpl
import com.example.mobilecinema.domain.UseCase
import com.example.mobilecinema.domain.repository.GenreRepository
import com.example.mobilecinema.domain.use_case.genre.AddGenreUseCase.Response
import com.example.mobilecinema.domain.use_case.genre.DeleteAllGenresUseCase.Request
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetGenreUseCase(
    private val genreRepository: GenreRepository = GenreRepositoryImpl(),
    configuration: Configuration = Configuration(Dispatchers.IO),
) : UseCase<GetGenreUseCase.Request, GetGenreUseCase.Response>(configuration) {

    override fun process(request: Request): Flow<Response> =

        genreRepository.getGenres()
            .map {
                Response(it)
            }

    override fun defaultRequest(): Request {
        return Request()
    }

    class Request : UseCase.Request
    data class Response(val genres: List<GenreModel>) : UseCase.Response

}