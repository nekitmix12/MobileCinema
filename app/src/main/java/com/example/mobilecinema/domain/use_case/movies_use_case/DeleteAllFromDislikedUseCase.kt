package com.example.mobilecinema.domain.use_case.movies_use_case

import com.example.mobilecinema.data.model.auth.ProfileDTO
import com.example.mobilecinema.data.model.movie.MovieDetailsModel
import com.example.mobilecinema.domain.UseCase
import com.example.mobilecinema.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class DeleteAllFromDislikedUseCase(
    private val moviesRepository: MoviesRepository,
    configuration: Configuration,
) : UseCase<DeleteAllFromDislikedUseCase.Request, DeleteAllFromDislikedUseCase.Response>(configuration) {


    override fun process(request: Request): Flow<Response> = flow {
        emit(
            Response(
                (moviesRepository.deleteAll())
            )
        )
    }

    override fun defaultRequest(): Request {
        return Request()
    }

    class Request : UseCase.Request
    class Response(val putProfile: Unit) : UseCase.Response

}