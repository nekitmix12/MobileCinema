package com.example.mobilecinema.domain.converters

import com.example.mobilecinema.data.model.favorite_movies.MoviesListModel
import com.example.mobilecinema.data.model.kinopoisk_api.Film
import com.example.mobilecinema.data.model.movie.MovieDetailsModel
import com.example.mobilecinema.domain.Result
import com.example.mobilecinema.domain.use_case.UiState
import com.example.mobilecinema.domain.use_case.favorite_movies_use_case.GetFavoriteMoviesUseCase
import com.example.mobilecinema.domain.use_case.kinopoisk_use_case.GetFilmUseCase
import com.example.mobilecinema.domain.use_case.movies_use_case.AddFilmToDislikedUseCase
import com.example.mobilecinema.domain.use_case.movies_use_case.GetMoviesDetailsUseCase

class FilmToDislikedConverter {
    fun convert(
        addFilmResult: Result<AddFilmToDislikedUseCase.Response>
    ): UiState<Unit> {
        return when (addFilmResult) {
            is Result.Error -> UiState.Error(
                addFilmResult.exception.localizedMessage.orEmpty()
            )

            is Result.Success -> UiState.Success(
                addFilmResult.data.putProfile
            )
        }
    }
}