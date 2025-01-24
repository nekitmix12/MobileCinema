package com.example.mobilecinema.domain.converters

import com.example.mobilecinema.domain.Result
import com.example.mobilecinema.domain.use_case.UiState
import com.example.mobilecinema.domain.use_case.movies_use_case.AddFilmToDislikedUseCase

class AddFilmToDislikedConverter {
    fun convert(
        addFilmResult: Result<AddFilmToDislikedUseCase.Response>
    ): UiState<Unit> {
        return when (addFilmResult) {
            is Result.Error -> UiState.Error(
                addFilmResult.exception
            )

            is Result.Success -> UiState.Success(
                addFilmResult.data.putProfile
            )
        }
    }
}