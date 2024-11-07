package com.example.mobilecinema.domain.use_case.movies_use_case

import com.example.mobilecinema.data.model.movie.MovieDetailsModel
import com.example.mobilecinema.domain.UseCase
import com.example.mobilecinema.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class GetMoviesDetailsUseCase(
    private val moviesRepository: MoviesRepository,
    configuration: Configuration,
) : UseCase<GetMoviesDetailsUseCase.Request, GetMoviesDetailsUseCase.Response>(configuration) {

    override fun process(request: Request): Flow<Response> =
            moviesRepository.getDetails(request.id)
                .map {
                    Response(it)
                }


    data class Request(val id: String) : UseCase.Request
    data class Response(val review: MovieDetailsModel) : UseCase.Response

}