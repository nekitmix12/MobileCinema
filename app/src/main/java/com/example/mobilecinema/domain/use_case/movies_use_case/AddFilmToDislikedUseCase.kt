package com.example.mobilecinema.domain.use_case.movies_use_case

import com.example.mobilecinema.data.model.auth.ProfileDTO
import com.example.mobilecinema.data.model.movie.ShortMovieModel
import com.example.mobilecinema.domain.UseCase
import com.example.mobilecinema.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AddFilmToDislikedUseCase(
    private val moviesRepository: MoviesRepository,
    configuration: Configuration
):UseCase<AddFilmToDislikedUseCase.Request,AddFilmToDislikedUseCase.Response>(configuration) {

    override fun process(request: Request): Flow<Response> = flow {
        emit(
            Response(
                (moviesRepository.addFilm(request.moviesModel))
            )
        )
    }


    data class Request(val moviesModel: ShortMovieModel) : UseCase.Request
    class Response(val putProfile: Unit) : UseCase.Response
}