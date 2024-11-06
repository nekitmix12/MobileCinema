package com.example.mobilecinema.domain.converters.film

import com.example.mobilecinema.data.model.favorite_movies.MoviesListModel
import com.example.mobilecinema.domain.Result
import com.example.mobilecinema.domain.use_case.UiState
import com.example.mobilecinema.domain.use_case.favorite_movies_use_case.GetFavoriteMoviesUseCase

class FavoriteMoviesConverter {
    fun convert(
        getFavoriteMovieResult: Result<GetFavoriteMoviesUseCase.Response>
    ): UiState<MoviesListModel> {
        return when (getFavoriteMovieResult) {
            is Result.Error -> UiState.Error(
                getFavoriteMovieResult.exception.localizedMessage.orEmpty()
            )

            is Result.Success -> UiState.Success(
                MoviesListModel(
                    movies = getFavoriteMovieResult.data.moviesListModel.movies
                )
            )
        }
    }
}