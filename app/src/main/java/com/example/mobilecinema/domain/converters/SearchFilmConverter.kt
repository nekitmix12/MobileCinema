package com.example.mobilecinema.domain.converters

import com.example.mobilecinema.data.model.favorite_movies.MoviesListModel
import com.example.mobilecinema.data.model.kinopoisk_api.SearchFilmDto
import com.example.mobilecinema.data.model.kinopoisk_api.SearchFilmResponse
import com.example.mobilecinema.domain.Result
import com.example.mobilecinema.domain.use_case.UiState
import com.example.mobilecinema.domain.use_case.favorite_movies_use_case.GetFavoriteMoviesUseCase
import com.example.mobilecinema.domain.use_case.kinopoisk_use_case.SearchFilmUseCase

class SearchFilmConverter {
    fun convert(
        searchFilmResult: Result<SearchFilmUseCase.Response>
    ): UiState<SearchFilmResponse> {
        return when (searchFilmResult) {
            is Result.Error -> UiState.Error(
                searchFilmResult.exception.localizedMessage.orEmpty()
            )

            is Result.Success -> UiState.Success(
                searchFilmResult.data.answer
            )
        }
    }
}