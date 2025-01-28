package com.example.mobilecinema.presentation.movies_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mobilecinema.domain.converters.FilmConverter
import com.example.mobilecinema.domain.converters.AddFilmToDislikedConverter
import com.example.mobilecinema.domain.converters.MovieDetailsConverter
import com.example.mobilecinema.domain.converters.MoviesRatingConverter
import com.example.mobilecinema.domain.converters.SearchFilmConverter
import com.example.mobilecinema.domain.converters.film.AddFilmToFavoriteConverter
import com.example.mobilecinema.domain.converters.film.DeleteFilmFromFavoriteConverter
import com.example.mobilecinema.domain.converters.film.FavoriteMoviesConverter
import com.example.mobilecinema.domain.converters.genre.AddGenreToFavoriteConverter
import com.example.mobilecinema.domain.converters.genre.DeleteGenreFromFavoriteConverter
import com.example.mobilecinema.domain.converters.genre.GetGenresFromFavoriteConverter
import com.example.mobilecinema.domain.use_case.favorite_movies_use_case.AddFavoriteMovieUseCase
import com.example.mobilecinema.domain.use_case.favorite_movies_use_case.DeleteFavoriteMovieUseCase
import com.example.mobilecinema.domain.use_case.favorite_movies_use_case.GetFavoriteMoviesUseCase
import com.example.mobilecinema.domain.use_case.genre.AddGenreUseCase
import com.example.mobilecinema.domain.use_case.genre.DeleteGenreUseCase
import com.example.mobilecinema.domain.use_case.genre.GetGenreUseCase
import com.example.mobilecinema.domain.use_case.kinopoisk_use_case.GetFilmUseCase
import com.example.mobilecinema.domain.use_case.kinopoisk_use_case.SearchFilmUseCase
import com.example.mobilecinema.domain.use_case.movies_use_case.AddFilmToDislikedUseCase
import com.example.mobilecinema.domain.use_case.movies_use_case.GetMoviesDetailsUseCase
import com.example.mobilecinema.domain.use_case.movies_use_case.MoviesRatingUseCase
import com.example.mobilecinema.domain.use_case.review_use_case.AddReviewUseCase
import com.example.mobilecinema.domain.use_case.review_use_case.DeleteReviewUseCase
import com.example.mobilecinema.domain.use_case.review_use_case.EditReviewUseCase

class MoviesDetailViewModelFactory(
    private val addReviewUseCase: AddReviewUseCase = AddReviewUseCase(),
    private val editReviewUseCase: EditReviewUseCase = EditReviewUseCase(),
    private val deleteReviewUseCase: DeleteReviewUseCase = DeleteReviewUseCase(),
    private val getFilmUseCase: GetFilmUseCase = GetFilmUseCase(),
    private val searchFilmUseCase: SearchFilmUseCase = SearchFilmUseCase(),
    private val getMoviesDetailsUseCase: GetMoviesDetailsUseCase = GetMoviesDetailsUseCase(),
    private val movieDetailsConverter: MovieDetailsConverter = MovieDetailsConverter(),
    private val searchFilmConverter: SearchFilmConverter = SearchFilmConverter(),
    private val filmConverter: FilmConverter = FilmConverter(),
    private val moviesRatingConverter: MoviesRatingConverter = MoviesRatingConverter(),
    private val moviesRatingUseCase: MoviesRatingUseCase = MoviesRatingUseCase(),
    private val addFilmToDislikedUseCase: AddFilmToDislikedUseCase = AddFilmToDislikedUseCase(),
    private val addFilmToDislikedConverter: AddFilmToDislikedConverter = AddFilmToDislikedConverter(),
    private val addFilmToFavoriteUseCase: AddFavoriteMovieUseCase = AddFavoriteMovieUseCase(),
    private val addFavoriteMovieConverter: AddFilmToFavoriteConverter = AddFilmToFavoriteConverter(),
    private val getGenreUseCase: GetGenreUseCase = GetGenreUseCase(),
    private val getGenresFromFavoriteConverter: GetGenresFromFavoriteConverter = GetGenresFromFavoriteConverter(),
    private val addGenreUseCase: AddGenreUseCase = AddGenreUseCase(),
    private val addFavoriteGenreConverter: AddGenreToFavoriteConverter = AddGenreToFavoriteConverter(),
     private val deleteGenreUseCase: DeleteGenreUseCase = DeleteGenreUseCase(),
    private val deleteGenreFromFavoriteConverter: DeleteGenreFromFavoriteConverter = DeleteGenreFromFavoriteConverter(),
    private val getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase = GetFavoriteMoviesUseCase(),
    private val getFavoriteMoviesConverter: FavoriteMoviesConverter = FavoriteMoviesConverter(),
    private val deleteFavoriteMovieUseCase: DeleteFavoriteMovieUseCase = DeleteFavoriteMovieUseCase(),
    private val deleteFavoriteMoviesConverter: DeleteFilmFromFavoriteConverter = DeleteFilmFromFavoriteConverter(),
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MoviesDetailViewModel::class.java)) {
            return MoviesDetailViewModel(
                addReviewUseCase,
                editReviewUseCase,
                deleteReviewUseCase,
                getFilmUseCase,
                searchFilmUseCase,
                searchFilmConverter,
                getMoviesDetailsUseCase,
                movieDetailsConverter,
                filmConverter,
                addFilmToDislikedUseCase,
                addFilmToDislikedConverter,
                addFilmToFavoriteUseCase,
                addFavoriteMovieConverter,
                addGenreUseCase,
                addFavoriteGenreConverter,
                getGenreUseCase,
                getGenresFromFavoriteConverter,
                deleteGenreUseCase,
                deleteGenreFromFavoriteConverter,
                getFavoriteMoviesUseCase,
                getFavoriteMoviesConverter,
                deleteFavoriteMovieUseCase,
                deleteFavoriteMoviesConverter
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}