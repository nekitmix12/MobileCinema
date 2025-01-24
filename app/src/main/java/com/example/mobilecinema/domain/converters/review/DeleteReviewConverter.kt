package com.example.mobilecinema.domain.converters.review

import com.example.mobilecinema.data.model.favorite_movies.MoviesListModel
import com.example.mobilecinema.data.model.kinopoisk_api.Film
import com.example.mobilecinema.data.model.movie.MovieDetailsModel
import com.example.mobilecinema.domain.Result
import com.example.mobilecinema.domain.use_case.UiState
import com.example.mobilecinema.domain.use_case.favorite_movies_use_case.GetFavoriteMoviesUseCase
import com.example.mobilecinema.domain.use_case.kinopoisk_use_case.GetFilmUseCase
import com.example.mobilecinema.domain.use_case.movies_use_case.GetMoviesDetailsUseCase
import com.example.mobilecinema.domain.use_case.review_use_case.DeleteReviewUseCase

class DeleteReviewConverter {
    fun convert(
        getFilmResult: Result<DeleteReviewUseCase.Response>
    ): UiState<Unit> {
        return when (getFilmResult) {
            is Result.Error -> UiState.Error(
                getFilmResult.exception
            )

            is Result.Success -> UiState.Success(
                getFilmResult.data.answer
            )
        }
    }
}