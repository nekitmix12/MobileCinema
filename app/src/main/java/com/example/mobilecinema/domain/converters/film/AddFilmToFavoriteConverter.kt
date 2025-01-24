package com.example.mobilecinema.domain.converters.film

import com.example.mobilecinema.domain.Result
import com.example.mobilecinema.domain.use_case.UiState
import com.example.mobilecinema.domain.use_case.favorite_movies_use_case.AddFavoriteMovieUseCase

class AddFilmToFavoriteConverter {
    fun convert(
        addFavoriteResult: Result<AddFavoriteMovieUseCase.Response>
    ): UiState<Unit> {
        return when (addFavoriteResult) {
            is Result.Error -> UiState.Error(
                addFavoriteResult.exception
            )

            is Result.Success -> UiState.Success(
                addFavoriteResult.data.answer
            )
        }
    }
}