package com.example.mobilecinema.domain.use_case.movies_use_case

import com.example.mobilecinema.data.model.kinopoisk_api.Film
import com.example.mobilecinema.data.model.movie.MovieDetailsModel
import com.example.mobilecinema.data.model.movie.ShortMovieModel
import com.example.mobilecinema.domain.UseCase
import com.example.mobilecinema.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class GetFilmFromDislikedUseCase(
    private val moviesRepository: MoviesRepository,
    configuration: Configuration,
) : UseCase<GetFilmFromDislikedUseCase.Request, GetFilmFromDislikedUseCase.Response>(configuration) {

    override fun process(request: Request): Flow<Response> =
            moviesRepository.getFilms()
                .map {
                    Response(it)
                }

    override fun defaultRequest(): Request {
        return Request()
    }
    class Request() : UseCase.Request
    data class Response(val film: List<ShortMovieModel>) : UseCase.Response

}