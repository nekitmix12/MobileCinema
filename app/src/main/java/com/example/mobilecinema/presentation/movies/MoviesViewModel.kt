package com.example.mobilecinema.presentation.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilecinema.data.model.favorite_movies.MoviesListModel
import com.example.mobilecinema.data.model.movie.MoviesPagedListModel
import com.example.mobilecinema.domain.MoviesFilmRating
import com.example.mobilecinema.domain.converters.FavoriteMoviesConverter
import com.example.mobilecinema.domain.use_case.auth_use_case.UiState
import com.example.mobilecinema.domain.use_case.favorite_movies_use_case.GetFavoriteMoviesUseCase
import com.example.mobilecinema.domain.use_case.movies_use_case.GetMoviesPageUseCase
import com.example.mobilecinema.domain.converters.MoviesConverter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MoviesViewModel(
    private val useCase: GetMoviesPageUseCase,
    private val converter: MoviesConverter,
    private val getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase,
    private val favoriteMoviesConverter: FavoriteMoviesConverter,
    private val favoritesMoviesFilmRating: MoviesFilmRating
) : ViewModel() {
    private val _movies = MutableStateFlow<UiState<MoviesPagedListModel>>(UiState.Loading)
    val movies: StateFlow<UiState<MoviesPagedListModel>> = _movies

    private val _moviesFavorite = MutableStateFlow<UiState<MoviesListModel>>(UiState.Loading)
    val moviesFavorite: StateFlow<UiState<MoviesListModel>> = _moviesFavorite

    private var id = 1

    suspend fun loadMovies(){
        viewModelScope.launch {
            useCase.execute(
                GetMoviesPageUseCase.Request(
                    pageData = id
                )
            )
                .map {
                    converter.convert(it)
                }

                .collect {
                    _movies.value = it

                }

        }
    }

    suspend fun loadFavoritesMovies(){
        viewModelScope.launch {
            getFavoriteMoviesUseCase.execute()
                .map {
                    favoriteMoviesConverter.convert(it)
                }
                .collect {
                    _moviesFavorite.value = it
                }
        }
    }

    fun incId(){
        id += 1
    }
    fun setCommonId(){
        id = 1
    }
}