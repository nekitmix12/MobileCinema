package com.example.mobilecinema.domain.converters.genre

import com.example.mobilecinema.domain.Result
import com.example.mobilecinema.domain.use_case.UiState
import com.example.mobilecinema.domain.use_case.genre.AddGenreUseCase
import com.example.mobilecinema.domain.use_case.genre.DeleteGenreUseCase

class DeleteGenreFromFavoriteConverter {
    fun convert(
        deleteGenreResult: Result<DeleteGenreUseCase.Response>
    ): UiState<Unit> {
        return when (deleteGenreResult) {
            is Result.Error -> UiState.Error(
                deleteGenreResult.exception
            )

            is Result.Success -> UiState.Success(
                deleteGenreResult.data.answer
            )
        }
    }
}