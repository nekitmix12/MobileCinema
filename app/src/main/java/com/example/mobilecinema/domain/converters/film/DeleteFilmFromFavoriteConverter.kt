package com.example.mobilecinema.domain.converters.film

import com.example.mobilecinema.domain.Result
import com.example.mobilecinema.domain.use_case.UiState
import com.example.mobilecinema.domain.use_case.favorite_movies_use_case.AddFavoriteMovieUseCase
import com.example.mobilecinema.domain.use_case.favorite_movies_use_case.DeleteFavoriteMovieUseCase

class DeleteFilmFromFavoriteConverter {
    fun convert(
        deleteFavoriteResult: Result<DeleteFavoriteMovieUseCase.Response>
    ): UiState<Unit> {
        return when (deleteFavoriteResult) {
            is Result.Error -> UiState.Error(
                deleteFavoriteResult.exception.localizedMessage.orEmpty()
            )

            is Result.Success -> UiState.Success(
                deleteFavoriteResult.data.answer
            )
        }
    }
}