package com.example.mobilecinema.presentation.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilecinema.data.model.favorite_movies.MoviesListModel
import com.example.mobilecinema.data.model.movie.MoviesPagedListModel
import com.example.mobilecinema.domain.converters.film.FavoriteMoviesConverter
import com.example.mobilecinema.domain.converters.film.MoviesConverter
import com.example.mobilecinema.domain.converters.MoviesRatingConverter
import com.example.mobilecinema.domain.use_case.UiState
import com.example.mobilecinema.domain.use_case.favorite_movies_use_case.GetFavoriteMoviesUseCase
import com.example.mobilecinema.domain.use_case.movies_use_case.MoviesRatingUseCase
import com.example.mobilecinema.domain.use_case.movies_use_case.GetMoviesPageUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MoviesViewModel(
    private val useCase: GetMoviesPageUseCase,
    private val converter: MoviesConverter,
    private val getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase,
    private val favoriteMoviesConverter: FavoriteMoviesConverter,
    private val moviesFilmRating: MoviesRatingUseCase,
    private val moviesRatingConverter: MoviesRatingConverter
) : ViewModel() {
    private val _movies = MutableStateFlow<UiState<MoviesPagedListModel>>(UiState.Loading)
    val movies: StateFlow<UiState<MoviesPagedListModel>> = _movies

    private val _moviesFavorite = MutableStateFlow<UiState<MoviesListModel>>(UiState.Loading)
    val moviesFavorite: StateFlow<UiState<MoviesListModel>> = _moviesFavorite

    private val _moviesRating = MutableStateFlow<UiState<List<Float>>>(UiState.Loading)
    val moviesRating: StateFlow<UiState<List<Float>>> = _moviesRating


    private var id = 1

    suspend fun loadMovies() {
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

    suspend fun loadFavoritesMovies() {
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

    suspend fun loadRatings() {

        viewModelScope.launch {
            movies.collect { it1 ->
                when (it1) {
                    is UiState.Loading -> {}
                    is UiState.Error -> {}
                    is UiState.Success -> {
                        moviesFilmRating.execute(
                            it1.data.movies?.let {
                                MoviesRatingUseCase.Request(
                                    movies = it
                                )

                            }
                        ).map {
                            moviesRatingConverter.convert(it)
                        }.collect{
                            _moviesRating.value = it
                        }
                    }
                }
            }
        }

    }

    fun incId() {
        id += 1
    }

    fun setCommonId() {
        id = 1
    }
}