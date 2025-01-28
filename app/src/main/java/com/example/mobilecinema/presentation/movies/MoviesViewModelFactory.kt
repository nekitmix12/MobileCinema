package com.example.mobilecinema.presentation.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mobilecinema.domain.converters.MoviesRatingConverter
import com.example.mobilecinema.domain.converters.film.FavoriteMoviesConverter
import com.example.mobilecinema.domain.converters.film.MoviesConverter
import com.example.mobilecinema.domain.converters.genre.AddGenreToFavoriteConverter
import com.example.mobilecinema.domain.converters.genre.DeleteGenreFromFavoriteConverter
import com.example.mobilecinema.domain.converters.genre.GetGenresFromFavoriteConverter
import com.example.mobilecinema.domain.use_case.favorite_movies_use_case.GetFavoriteMoviesUseCase
import com.example.mobilecinema.domain.use_case.genre.AddGenreUseCase
import com.example.mobilecinema.domain.use_case.genre.DeleteGenreUseCase
import com.example.mobilecinema.domain.use_case.genre.GetGenreUseCase
import com.example.mobilecinema.domain.use_case.movies_use_case.GetMoviesPageUseCase
import com.example.mobilecinema.domain.use_case.movies_use_case.MoviesRatingUseCase

class MoviesViewModelFactory(
    private val getMoviesPageUseCase: GetMoviesPageUseCase = GetMoviesPageUseCase(),
    private val moviesConverter: MoviesConverter = MoviesConverter(),
    private val getGenreUseCase: GetGenreUseCase = GetGenreUseCase(),
    private val getGenresFromFavoriteConverter: GetGenresFromFavoriteConverter = GetGenresFromFavoriteConverter(),
    private val getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase = GetFavoriteMoviesUseCase(),
    private val favoriteMoviesConverter: FavoriteMoviesConverter = FavoriteMoviesConverter(),
    private val moviesRatingUseCase: MoviesRatingUseCase = MoviesRatingUseCase(),
    private val moviesRatingConverter: MoviesRatingConverter = MoviesRatingConverter(),
    private val addGenreUseCase: AddGenreUseCase = AddGenreUseCase(),
    private val addFavoriteGenreConverter: AddGenreToFavoriteConverter = AddGenreToFavoriteConverter(),
    private val deleteGenreUseCase: DeleteGenreUseCase = DeleteGenreUseCase(),
    private val deleteGenreFromFavoriteConverter: DeleteGenreFromFavoriteConverter = DeleteGenreFromFavoriteConverter(),

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
                moviesRatingConverter = moviesRatingConverter,
                deleteGenreUseCase,
                deleteGenreFromFavoriteConverter,
                addGenreUseCase,
                addFavoriteGenreConverter,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}