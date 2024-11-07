package com.example.mobilecinema.presentation.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mobilecinema.domain.converters.film.FavoriteMoviesConverter
import com.example.mobilecinema.domain.converters.film.MoviesConverter
import com.example.mobilecinema.domain.converters.MoviesRatingConverter
import com.example.mobilecinema.domain.converters.genre.DeleteGenreFromFavoriteConverter
import com.example.mobilecinema.domain.converters.genre.GetGenresFromFavoriteConverter
import com.example.mobilecinema.domain.use_case.favorite_movies_use_case.GetFavoriteMoviesUseCase
import com.example.mobilecinema.domain.use_case.genre.DeleteGenreUseCase
import com.example.mobilecinema.domain.use_case.genre.GetGenreUseCase
import com.example.mobilecinema.domain.use_case.movies_use_case.MoviesRatingUseCase
import com.example.mobilecinema.domain.use_case.movies_use_case.GetMoviesPageUseCase

class FavoriteMoviesViewModelFactory(
    private val getGenreUseCase: GetGenreUseCase,
    private val getGenresFromFavoriteConverter: GetGenresFromFavoriteConverter,
    private val deleteGenreUseCase: DeleteGenreUseCase,
    private val deleteGenreFromFavoriteConverter: DeleteGenreFromFavoriteConverter,
    private val getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase,
    private val favoriteMoviesConverter: FavoriteMoviesConverter,
    private val moviesRatingUseCase: MoviesRatingUseCase,
    private val moviesRatingConverter: MoviesRatingConverter
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(
                getFavoriteMoviesUseCase = getFavoriteMoviesUseCase,
                getFavoriteMoviesConverter = favoriteMoviesConverter,
                moviesFilmRating = moviesRatingUseCase,
                moviesRatingConverter = moviesRatingConverter,
                deleteGenreUseCase = deleteGenreUseCase,
                deleteGenreFromFavoriteConverter = deleteGenreFromFavoriteConverter,
                getGenresFromFavoriteConverter = getGenresFromFavoriteConverter,
                getGenreUseCase = getGenreUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}