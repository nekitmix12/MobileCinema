package com.example.mobilecinema.domain.use_case.movies_use_case

import com.example.mobilecinema.data.model.auth.ProfileDTO
import com.example.mobilecinema.data.model.movie.MovieDetailsModel
import com.example.mobilecinema.data.model.movie.ShortMovieModel
import com.example.mobilecinema.data.repository.MoviesRepositoryImpl
import com.example.mobilecinema.domain.UseCase
import com.example.mobilecinema.domain.repository.MoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class DeleteFilmFromDislikedUseCase(
    private val moviesRepository: MoviesRepository = MoviesRepositoryImpl(),
    configuration: Configuration = Configuration(Dispatchers.IO),
) : UseCase<DeleteFilmFromDislikedUseCase.Request, DeleteFilmFromDislikedUseCase.Response>(configuration) {


    override fun process(request: Request): Flow<Response> = flow {
        emit(
            Response(
                (moviesRepository.deleteFilm(request.filmModel))
            )
        )
    }


    data class Request(val filmModel: ShortMovieModel) : UseCase.Request
    class Response(val putProfile: Unit) : UseCase.Response

}