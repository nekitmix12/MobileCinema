package com.example.mobilecinema.presentation.favorite

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilecinema.data.model.favorite_movies.MoviesListModel
import com.example.mobilecinema.data.model.movie.GenreModel
import com.example.mobilecinema.data.model.movie.MovieElementModel
import com.example.mobilecinema.domain.converters.MoviesRatingConverter
import com.example.mobilecinema.domain.converters.film.FavoriteMoviesConverter
import com.example.mobilecinema.domain.converters.genre.DeleteGenreFromFavoriteConverter
import com.example.mobilecinema.domain.converters.genre.GetGenresFromFavoriteConverter
import com.example.mobilecinema.domain.use_case.UiState
import com.example.mobilecinema.domain.use_case.favorite_movies_use_case.AddFavoriteMovieUseCase
import com.example.mobilecinema.domain.use_case.favorite_movies_use_case.GetFavoriteMoviesUseCase
import com.example.mobilecinema.domain.use_case.genre.DeleteGenreUseCase
import com.example.mobilecinema.domain.use_case.genre.GetGenreUseCase
import com.example.mobilecinema.domain.use_case.movies_use_case.MoviesRatingUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val getGenreUseCase: GetGenreUseCase,
    private val getGenresFromFavoriteConverter: GetGenresFromFavoriteConverter,
    private val deleteGenreUseCase: DeleteGenreUseCase,
    private val deleteGenreFromFavoriteConverter: DeleteGenreFromFavoriteConverter,
    private val getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase,
    private val getFavoriteMoviesConverter: FavoriteMoviesConverter,
    private val moviesFilmRating: MoviesRatingUseCase,
    private val moviesRatingConverter: MoviesRatingConverter
) : ViewModel(){
    private val _allFavoriteGenre = MutableStateFlow<UiState<List<GenreModel>>>(UiState.Loading)
    val allFavoriteGenres : StateFlow<UiState<List<GenreModel>>> = _allFavoriteGenre

    private val _favoriteMovies = MutableStateFlow<UiState<MoviesListModel>>(UiState.Loading)
    val favoriteMovies :StateFlow<UiState<MoviesListModel>> = _favoriteMovies

    private val _moviesRating = MutableStateFlow<UiState<List<Float>>>(UiState.Loading)
    val moviesRating: StateFlow<UiState<List<Float>>> = _moviesRating

    fun getAllFavoriteGenres(){
        viewModelScope.launch {
            getGenreUseCase.execute()
                .map {
                    getGenresFromFavoriteConverter.convert(it)
                }
                .collect{
                    _allFavoriteGenre.value = it

                    when(it){
                        is UiState.Error -> {
                            Log.e("feed_vm",it.errorMessage)
                        }
                        UiState.Loading -> {
                            Log.d("feed_vm", "loading_all_genre")
                        }
                        is UiState.Success -> {
                        }
                    }
                }
        }
    }

    fun deleteFavoriteGenre(genreModel: GenreModel){
        viewModelScope.launch {
            deleteGenreUseCase.execute(
                DeleteGenreUseCase.Request(
                    genreModel
                )
            ).map{
                deleteGenreFromFavoriteConverter.convert(it)
            }.collect{
                when(it){
                    is UiState.Error -> {
                        Log.e("feed_vm",it.errorMessage)
                    }
                    UiState.Loading -> {
                        Log.d("feed_vm", "loading_all_genre")
                    }
                    is UiState.Success -> {
                        Log.e("feed_vm",it.data.toString()+"<_delete_>")

                    }
                }
            }
        }
        getAllFavoriteGenres()
    }

    fun getFavoritesFilms(){
        viewModelScope.launch {
            getFavoriteMoviesUseCase.execute()
                .map {
                    getFavoriteMoviesConverter.convert(it)
                }
                .collect{
                    Log.d("vm",it.toString())
                    _favoriteMovies.value = it
                }
        }
    }

    suspend fun loadRatings() {

        viewModelScope.launch {
            favoriteMovies.collect { it1 ->
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
}