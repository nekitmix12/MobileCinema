package com.example.mobilecinema.domain.converters

import com.example.mobilecinema.domain.Result
import com.example.mobilecinema.domain.use_case.UiState
import com.example.mobilecinema.domain.use_case.movies_use_case.MoviesRatingUseCase

class MoviesRatingConverter {
    fun convert(
        moviesRatingResult: Result<MoviesRatingUseCase.
        Response>
    ): UiState<List<Float>> {
        return when (moviesRatingResult) {
            is Result.Error -> {
                UiState.Error(
                    moviesRatingResult.exception
                )
            }

            is Result.Success -> {
                UiState.Success(
                    moviesRatingResult.data.newRatings
                )
            }
        }
    }
}