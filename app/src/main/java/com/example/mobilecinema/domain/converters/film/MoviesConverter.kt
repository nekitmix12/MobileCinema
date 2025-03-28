package com.example.mobilecinema.domain.converters.film

import com.example.mobilecinema.data.model.movie.MoviesPagedListModel
import com.example.mobilecinema.domain.Result
import com.example.mobilecinema.domain.use_case.UiState
import com.example.mobilecinema.domain.use_case.movies_use_case.GetMoviesPageUseCase


class MoviesConverter {
    fun convert(
        getMoviesResult: Result<GetMoviesPageUseCase.
        Response>
    ): UiState<MoviesPagedListModel> {
        return when (getMoviesResult) {
            is Result.Error -> {
                UiState.Error(
                    getMoviesResult.exception
                )
            }

            is Result.Success -> {
                UiState.Success(
                    MoviesPagedListModel(
                        getMoviesResult.data.moviesPagedListModel.movies,
                        getMoviesResult.data.moviesPagedListModel.pageInfo
                    )
                )
            }
        }
    }
}