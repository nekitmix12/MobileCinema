package com.example.mobilecinema.domain.converters

import com.example.mobilecinema.data.model.favorite_movies.MoviesListModel
import com.example.mobilecinema.data.model.movie.MovieDetailsModel
import com.example.mobilecinema.domain.Result
import com.example.mobilecinema.domain.use_case.UiState
import com.example.mobilecinema.domain.use_case.favorite_movies_use_case.GetFavoriteMoviesUseCase
import com.example.mobilecinema.domain.use_case.movies_use_case.GetMoviesDetailsUseCase

class MovieDetailsConverter {
    fun convert(
        getMovieDetailsResult: Result<GetMoviesDetailsUseCase.Response>
    ): UiState<MovieDetailsModel> {
        return when (getMovieDetailsResult) {
            is Result.Error -> UiState.Error(
                getMovieDetailsResult.exception
            )

            is Result.Success -> UiState.Success(
                getMovieDetailsResult.data.review
            )
        }
    }
}