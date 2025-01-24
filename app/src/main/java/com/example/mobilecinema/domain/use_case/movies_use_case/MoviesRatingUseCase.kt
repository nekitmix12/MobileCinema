package com.example.mobilecinema.domain.use_case.movies_use_case

import com.example.mobilecinema.data.model.movie.MovieElementModel
import com.example.mobilecinema.domain.MoviesFilmRating
import com.example.mobilecinema.domain.MoviesFilmRatingImpl
import com.example.mobilecinema.domain.UseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MoviesRatingUseCase(
    private val moviesFilmRating: MoviesFilmRating = MoviesFilmRatingImpl(),
    configuration: Configuration = Configuration(Dispatchers.IO),
) : UseCase<MoviesRatingUseCase.Request,
        MoviesRatingUseCase.Response>(configuration) {

    override fun process(request: Request): Flow<Response> {
        return flow {
            val ratings = moviesFilmRating.getFilmRating(
                request.movies
            )
            emit(Response(ratings))
        }

    }


    data class Request(
        val movies:List<MovieElementModel>
    ) : UseCase.Request

    data class Response(val newRatings: List<Float>) : UseCase.Response


}