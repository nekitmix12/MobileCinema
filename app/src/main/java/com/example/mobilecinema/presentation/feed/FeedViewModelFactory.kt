package com.example.mobilecinema.presentation.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mobilecinema.domain.converters.AddFilmToDislikedConverter
import com.example.mobilecinema.domain.converters.film.AddFilmToFavoriteConverter
import com.example.mobilecinema.domain.converters.film.MoviesConverter
import com.example.mobilecinema.domain.converters.genre.AddGenreToFavoriteConverter
import com.example.mobilecinema.domain.converters.genre.DeleteGenreFromFavoriteConverter
import com.example.mobilecinema.domain.converters.genre.GetGenresFromFavoriteConverter
import com.example.mobilecinema.domain.use_case.favorite_movies_use_case.AddFavoriteMovieUseCase
import com.example.mobilecinema.domain.use_case.genre.AddGenreUseCase
import com.example.mobilecinema.domain.use_case.genre.DeleteGenreUseCase
import com.example.mobilecinema.domain.use_case.genre.GetGenreUseCase
import com.example.mobilecinema.domain.use_case.movies_use_case.AddFilmToDislikedUseCase
import com.example.mobilecinema.domain.use_case.movies_use_case.GetMoviesPageUseCase


class FeedViewModelFactory(private val getMoviesPageUseCase: GetMoviesPageUseCase = GetMoviesPageUseCase(),
                           private val moviesConverter: MoviesConverter = MoviesConverter(),
                           private val addFilmToDislikedUseCase: AddFilmToDislikedUseCase = AddFilmToDislikedUseCase(),
                           private val filmConverter: AddFilmToDislikedConverter= AddFilmToDislikedConverter(),
                           private val addFilmToFavoriteUseCase: AddFavoriteMovieUseCase=AddFavoriteMovieUseCase(),
                           private val addFavoriteMovieConverter: AddFilmToFavoriteConverter=AddFilmToFavoriteConverter(),
                           private val addGenreUseCase: AddGenreUseCase=AddGenreUseCase(),
                           private val addFavoriteGenreConverter: AddGenreToFavoriteConverter=AddGenreToFavoriteConverter(),
                           private val getGenreUseCase: GetGenreUseCase=GetGenreUseCase(),
                           private val getGenresFromFavoriteConverter: GetGenresFromFavoriteConverter=GetGenresFromFavoriteConverter(),
                           private val deleteGenreUseCase: DeleteGenreUseCase=DeleteGenreUseCase(),
                           private val deleteGenreFromFavoriteConverter: DeleteGenreFromFavoriteConverter=DeleteGenreFromFavoriteConverter(),
                           ) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FeedViewModel::class.java)) {
            return FeedViewModel(
                useCase = getMoviesPageUseCase,
                converter = moviesConverter,
                addFilmToDislikedUseCase = addFilmToDislikedUseCase,
                filmConverter = filmConverter,
                addFilmToFavoriteUseCase= addFilmToFavoriteUseCase,
                addFavoriteMovieConverter = addFavoriteMovieConverter,
                addGenreUseCase = addGenreUseCase,
                addFavoriteGenreConverter = addFavoriteGenreConverter,
                getGenreUseCase = getGenreUseCase,
                getGenresFromFavoriteConverter = getGenresFromFavoriteConverter,
                deleteGenreUseCase = deleteGenreUseCase,
                deleteGenreFromFavoriteConverter =deleteGenreFromFavoriteConverter
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}