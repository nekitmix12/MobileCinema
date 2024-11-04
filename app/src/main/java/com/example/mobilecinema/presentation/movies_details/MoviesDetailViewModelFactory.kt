package com.example.mobilecinema.presentation.movies_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mobilecinema.domain.converters.FilmConverter
import com.example.mobilecinema.domain.converters.MovieDetailsConverter
import com.example.mobilecinema.domain.converters.MoviesRatingConverter
import com.example.mobilecinema.domain.converters.SearchFilmConverter
import com.example.mobilecinema.domain.use_case.kinopoisk_use_case.GetFilmUseCase
import com.example.mobilecinema.domain.use_case.kinopoisk_use_case.SearchFilmUseCase
import com.example.mobilecinema.domain.use_case.movies_use_case.GetMoviesDetailsUseCase
import com.example.mobilecinema.domain.use_case.movies_use_case.MoviesRatingUseCase
import com.example.mobilecinema.domain.use_case.review_use_case.AddReviewUseCase
import com.example.mobilecinema.domain.use_case.review_use_case.DeleteReviewUseCase
import com.example.mobilecinema.domain.use_case.review_use_case.EditReviewUseCase

class MoviesDetailViewModelFactory(
    private val addReviewUseCase: AddReviewUseCase,
    private val editReviewUseCase: EditReviewUseCase,
    private val deleteReviewUseCase: DeleteReviewUseCase,
    private val getFilmUseCase: GetFilmUseCase,
    private val searchFilmUseCase: SearchFilmUseCase,
    private val getMoviesDetailsUseCase: GetMoviesDetailsUseCase,
    private val movieDetailsConverter: MovieDetailsConverter,
    private val searchFilmConverter: SearchFilmConverter,
    private val filmConverter: FilmConverter,
    private val moviesRatingConverter: MoviesRatingConverter,
    private val moviesRatingUseCase: MoviesRatingUseCase
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
                    getMoviesDetailsUseCase,
                    movieDetailsConverter,
                    searchFilmConverter,
                    filmConverter,
                    moviesRatingConverter,
                    moviesRatingUseCase
                ) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
}