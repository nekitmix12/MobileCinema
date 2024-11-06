package com.example.mobilecinema.domain.converters.genre

import com.example.mobilecinema.domain.Result
import com.example.mobilecinema.domain.use_case.UiState
import com.example.mobilecinema.domain.use_case.genre.AddGenreUseCase

class AddGenreToFavoriteConverter {
    fun convert(
        addGenreResult: Result<AddGenreUseCase.Response>
    ): UiState<Unit> {
        return when (addGenreResult) {
            is Result.Error -> UiState.Error(
                addGenreResult.exception.localizedMessage.orEmpty()
            )

            is Result.Success -> UiState.Success(
                addGenreResult.data.answer
            )
        }
    }
}