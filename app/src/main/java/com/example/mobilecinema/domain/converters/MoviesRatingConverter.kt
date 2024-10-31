package com.example.mobilecinema.domain.converters

import com.example.mobilecinema.data.model.movie.MoviesPagedListModel
import com.example.mobilecinema.domain.Result
import com.example.mobilecinema.domain.use_case.auth_use_case.UiState
import com.example.mobilecinema.domain.use_case.favorite_movies_use_case.MoviesRatingUseCase
import com.example.mobilecinema.domain.use_case.movies_use_case.GetMoviesPageUseCase

class MoviesRatingConverter {
    fun convert(
        moviesRatingResult: Result<MoviesRatingUseCase.
        Response>
    ): UiState<List<Float>> {
        return when (moviesRatingResult) {
            is Result.Error -> {
                UiState.Error(
                    moviesRatingResult.exception.localizedMessage.orEmpty()
                )
            }

            is Result.Success -> {
                UiState.Success(
                    moviesRatingResult.data.ratings
                )
            }
        }
    }
}