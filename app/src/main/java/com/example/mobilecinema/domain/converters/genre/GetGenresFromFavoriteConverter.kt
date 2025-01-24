package com.example.mobilecinema.domain.converters.genre

import com.example.mobilecinema.data.model.movie.GenreModel
import com.example.mobilecinema.domain.Result
import com.example.mobilecinema.domain.use_case.UiState
import com.example.mobilecinema.domain.use_case.genre.AddGenreUseCase
import com.example.mobilecinema.domain.use_case.genre.GetGenreUseCase

class GetGenresFromFavoriteConverter {
    fun convert(
       getGenresResult: Result<GetGenreUseCase.Response>
    ): UiState<List<GenreModel>> {
        return when (getGenresResult) {
            is Result.Error -> UiState.Error(
                getGenresResult.exception
            )

            is Result.Success -> UiState.Success(
                getGenresResult.data.genres
            )
        }
    }
}