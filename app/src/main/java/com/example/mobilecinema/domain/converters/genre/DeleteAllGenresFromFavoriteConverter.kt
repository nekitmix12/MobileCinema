package com.example.mobilecinema.domain.converters.genre

import com.example.mobilecinema.domain.Result
import com.example.mobilecinema.domain.use_case.UiState
import com.example.mobilecinema.domain.use_case.genre.AddGenreUseCase
import com.example.mobilecinema.domain.use_case.genre.DeleteAllGenresUseCase

class DeleteAllGenresFromFavoriteConverter {
    fun convert(
        deleteGenresResult: Result<DeleteAllGenresUseCase.Response>
    ): UiState<Unit> {
        return when (deleteGenresResult) {
            is Result.Error -> UiState.Error(
                deleteGenresResult.exception
            )

            is Result.Success -> UiState.Success(
                deleteGenresResult.data.answer
            )
        }
    }
}