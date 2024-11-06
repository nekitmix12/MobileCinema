package com.example.mobilecinema.domain.use_case.genre

import android.util.Log
import com.example.mobilecinema.data.model.kinopoisk_api.Film
import com.example.mobilecinema.data.model.movie.GenreModel
import com.example.mobilecinema.domain.UseCase
import com.example.mobilecinema.domain.repository.GenreRepository
import com.example.mobilecinema.domain.repository.MoviesRepository
import com.example.mobilecinema.domain.use_case.genre.AddGenreUseCase.Response
import com.example.mobilecinema.domain.use_case.genre.DeleteAllGenresUseCase.Request
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class GetGenreUseCase(
    private val genreRepository: GenreRepository,
    configuration: Configuration,
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