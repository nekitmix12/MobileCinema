package com.example.mobilecinema.domain.use_case.favorite_movies_use_case

import com.example.mobilecinema.data.model.movie.MovieElementModel
import com.example.mobilecinema.domain.MoviesFilmRating
import com.example.mobilecinema.domain.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteMoviesRatingUseCase(
    private val favoritesMoviesFilmRating: MoviesFilmRating,
    configuration: Configuration
) : UseCase<FavoriteMoviesRatingUseCase.Request,
        FavoriteMoviesRatingUseCase.Response>(configuration) {

    override fun process(request: Request): Flow<Response> {
        return flow {
            val ratings = favoritesMoviesFilmRating.getFilmRating(request.movies)
            emit(Response(ratings))
        }

    }


    data class Request(val movies: List<MovieElementModel>):UseCase.Request
    data class Response(val ratings: List<Float>):UseCase.Response


}