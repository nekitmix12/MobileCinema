package com.example.mobilecinema.presentation.movies_details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilecinema.data.model.kinopoisk_api.Film
import com.example.mobilecinema.data.model.kinopoisk_api.SearchFilmResponse
import com.example.mobilecinema.data.model.movie.MovieDetailsModel
import com.example.mobilecinema.data.model.review.ReviewModel
import com.example.mobilecinema.domain.Result
import com.example.mobilecinema.domain.converters.FilmConverter
import com.example.mobilecinema.domain.converters.MovieDetailsConverter
import com.example.mobilecinema.domain.converters.MoviesRatingConverter
import com.example.mobilecinema.domain.converters.SearchFilmConverter
import com.example.mobilecinema.domain.use_case.UiState
import com.example.mobilecinema.domain.use_case.kinopoisk_use_case.GetFilmUseCase
import com.example.mobilecinema.domain.use_case.kinopoisk_use_case.SearchFilmUseCase
import com.example.mobilecinema.domain.use_case.movies_use_case.GetMoviesDetailsUseCase
import com.example.mobilecinema.domain.use_case.movies_use_case.MoviesRatingUseCase
import com.example.mobilecinema.domain.use_case.review_use_case.AddReviewUseCase
import com.example.mobilecinema.domain.use_case.review_use_case.DeleteReviewUseCase
import com.example.mobilecinema.domain.use_case.review_use_case.EditReviewUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MoviesDetailViewModel(
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
) : ViewModel() {

    private val _addDetailsResponse = MutableStateFlow<Result<AddReviewUseCase.Response>?>(null)
    val addDetailsResponse: StateFlow<Result<AddReviewUseCase.Response>?> =
        _addDetailsResponse

    private val _editDetailsResponse = MutableStateFlow<Result<EditReviewUseCase.Response>?>(null)
    val editDetailsResponse: StateFlow<Result<EditReviewUseCase.Response>?> =
        _editDetailsResponse

    private val _deleteDetailsResponse =
        MutableStateFlow<Result<DeleteReviewUseCase.Response>?>(null)
    val deleteDetailsResponse: StateFlow<Result<DeleteReviewUseCase.Response>?> =
        _deleteDetailsResponse

    private val _details = MutableStateFlow<UiState<MovieDetailsModel>>(UiState.Loading)
    val details: StateFlow<UiState<MovieDetailsModel>> = _details

    private val _searchResult = MutableStateFlow<UiState<SearchFilmResponse>>(UiState.Loading)
    val searchResult: StateFlow<UiState<SearchFilmResponse>> = _searchResult

    private val _film = MutableStateFlow<UiState<Film>>(UiState.Loading)
    val film: StateFlow<UiState<Film>> = _film

    private val _rating = MutableStateFlow<UiState<Float>>(UiState.Loading)
    val rating :StateFlow<UiState<Float>> = _rating


    var detail: MovieDetailsModel? = null

    var ratingCinema = 0f

    fun addReview(reviewModel: ReviewModel) {
        viewModelScope.launch {
            addReviewUseCase.execute().map {
                _addDetailsResponse.value = it
            }
        }
    }

    fun editReview() {
        viewModelScope.launch {
            editReviewUseCase.execute().map {
                _editDetailsResponse.value = it
            }
        }
    }

    fun deleteReview() {
        viewModelScope.launch {
            deleteReviewUseCase.execute().map {
                _deleteDetailsResponse.value = it
            }
        }
    }

    fun loadDetails(id: String) {
        viewModelScope.launch {
            getMoviesDetailsUseCase.execute(
                GetMoviesDetailsUseCase.Request(
                    id
                )
            ).map {
                movieDetailsConverter.convert(it)
            }.collect { el ->
                when (el) {
                    is UiState.Error -> {
                        Log.d("movies_details", el.errorMessage)
                    }

                    UiState.Loading -> {

                    }

                    is UiState.Success -> {
                        Log.d("movies_details", el.data.toString())
                        _details.value = el
                        detail = el.data
                        ratingCinema = getRating(el.data.reviews)
                        el.data.name?.let { searchFilm(it) }
                    }
                }

            }
        }
    }

    private fun searchFilm(name: String) {
        viewModelScope.launch {
            searchFilmUseCase.execute(
                SearchFilmUseCase.Request(
                    name, 1
                )
            ).map {
                searchFilmConverter.convert(it)
            }.collect { el ->
                when (el) {
                    is UiState.Error -> {
                        Log.d("movies_details", el.errorMessage)
                    }

                    UiState.Loading -> {

                    }

                    is UiState.Success -> {
                        Log.d("movies_details", el.data.toString())
                        _searchResult.value = el
                        detail?.let {
                            getFilmById(
                                findIdFromFilm(el.data, detail!!)
                            )
                        }
                    }
                }
            }
        }
    }

    private fun findIdFromFilm(
        searchFilmDto: SearchFilmResponse,
        moviesDetailsModel: MovieDetailsModel,
    ): Int {
        var id: Int = searchFilmDto.film[0].filmId
        for (element in searchFilmDto.film) {
            if (element.year == moviesDetailsModel.year) id = element.filmId
        }
        return id
    }

    private fun getFilmById(id: Int) {
        viewModelScope.launch {
            getFilmUseCase.execute(
                GetFilmUseCase.Request(id)
            ).map {
                filmConverter.convert(it)
            }.collect {
                when (it) {
                    is UiState.Error -> {
                        Log.d("movies_details", it.errorMessage)
                    }

                    UiState.Loading -> {

                    }

                    is UiState.Success -> {
                        Log.d("movies_details", it.data.toString())
                        _film.value = it
                    }
                }
            }
        }
    }

    private fun getRating(reviews: List<ReviewModel>?):Float{
        var rating  = 0f
        if (reviews != null) {
            for (review in reviews)
                rating+=review.rating.toFloat()
        }
        if (reviews != null) {
            return rating/reviews.size.toFloat()
        }
        return 0f
    }

}