package com.example.mobilecinema.presentation.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mobilecinema.domain.converters.film.FavoriteMoviesConverter
import com.example.mobilecinema.domain.converters.film.MoviesConverter
import com.example.mobilecinema.domain.converters.MoviesRatingConverter
import com.example.mobilecinema.domain.converters.genre.GetGenresFromFavoriteConverter
import com.example.mobilecinema.domain.use_case.favorite_movies_use_case.GetFavoriteMoviesUseCase
import com.example.mobilecinema.domain.use_case.genre.GetGenreUseCase
import com.example.mobilecinema.domain.use_case.movies_use_case.MoviesRatingUseCase
import com.example.mobilecinema.domain.use_case.movies_use_case.GetMoviesPageUseCase

class MoviesViewModelFactory(
    private val getMoviesPageUseCase: GetMoviesPageUseCase = GetMoviesPageUseCase(),
    private val moviesConverter: MoviesConverter = MoviesConverter(),
    private val getGenreUseCase: GetGenreUseCase = GetGenreUseCase(),
    private val getGenresFromFavoriteConverter: GetGenresFromFavoriteConverter = GetGenresFromFavoriteConverter(),
    private val getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase = GetFavoriteMoviesUseCase(),
    private val favoriteMoviesConverter: FavoriteMoviesConverter = FavoriteMoviesConverter(),
    private val moviesRatingUseCase: MoviesRatingUseCase = MoviesRatingUseCase(),
    private val moviesRatingConverter: MoviesRatingConverter = MoviesRatingConverter()
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MoviesViewModel::class.java)) {
            return MoviesViewModel(
                getMoviesPageUseCase = getMoviesPageUseCase,
                moviesConverter = moviesConverter,
                getGenreUseCase = getGenreUseCase,
                getGenresFromFavoriteConverter = getGenresFromFavoriteConverter,
                getFavoriteMoviesUseCase = getFavoriteMoviesUseCase,
                favoriteMoviesConverter = favoriteMoviesConverter,
                moviesFilmRating = moviesRatingUseCase,
                moviesRatingConverter = moviesRatingConverter
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}