package com.example.mobilecinema.presentation.feed

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilecinema.data.model.movie.GenreModel
import com.example.mobilecinema.data.model.movie.MovieElementModel
import com.example.mobilecinema.data.model.movie.MoviesPagedListModel
import com.example.mobilecinema.data.model.movie.ShortMovieModel
import com.example.mobilecinema.domain.converters.AddFilmToDislikedConverter
import com.example.mobilecinema.domain.converters.film.AddFilmToFavoriteConverter
import com.example.mobilecinema.domain.converters.film.MoviesConverter
import com.example.mobilecinema.domain.converters.genre.AddGenreToFavoriteConverter
import com.example.mobilecinema.domain.converters.genre.DeleteGenreFromFavoriteConverter
import com.example.mobilecinema.domain.converters.genre.GetGenresFromFavoriteConverter
import com.example.mobilecinema.domain.use_case.UiState
import com.example.mobilecinema.domain.use_case.favorite_movies_use_case.AddFavoriteMovieUseCase
import com.example.mobilecinema.domain.use_case.genre.AddGenreUseCase
import com.example.mobilecinema.domain.use_case.genre.DeleteGenreUseCase
import com.example.mobilecinema.domain.use_case.genre.GetGenreUseCase
import com.example.mobilecinema.domain.use_case.movies_use_case.AddFilmToDislikedUseCase
import com.example.mobilecinema.domain.use_case.movies_use_case.GetMoviesPageUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class FeedViewModel(
    private val useCase: GetMoviesPageUseCase,
    private val converter: MoviesConverter,
    private val addFilmToDislikedUseCase: AddFilmToDislikedUseCase,
    private val filmConverter: AddFilmToDislikedConverter,
    private val addFilmToFavoriteUseCase: AddFavoriteMovieUseCase,
    private val addFavoriteMovieConverter: AddFilmToFavoriteConverter,
    private val addGenreUseCase: AddGenreUseCase,
    private val addFavoriteGenreConverter: AddGenreToFavoriteConverter,
    private val getGenreUseCase: GetGenreUseCase,
    private val getGenresFromFavoriteConverter: GetGenresFromFavoriteConverter,
    private val deleteGenreUseCase: DeleteGenreUseCase,
    private val deleteGenreFromFavoriteConverter: DeleteGenreFromFavoriteConverter,
    ) : ViewModel() {




    private val _movies = MutableStateFlow<UiState<MoviesPagedListModel>>(UiState.Loading)
    val movies: StateFlow<UiState<MoviesPagedListModel>> = _movies

    private val _genres = MutableStateFlow<List<GenreModel>?>(null)
    val genres :StateFlow<List<GenreModel>?> = _genres

    private val _addFilmToDislikedResult = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val addFilmToDislikedResult :StateFlow<UiState<Unit>> = _addFilmToDislikedResult

    private val _addFilmToFavoriteResult = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val addFilmToFavoriteResult :StateFlow<UiState<Unit>> = _addFilmToFavoriteResult

    private val _allFavoriteGenre = MutableStateFlow<UiState<List<GenreModel>>>(UiState.Loading)
    val allFavoriteGenres :StateFlow<UiState<List<GenreModel>>> = _allFavoriteGenre

    private val _deleteFavoriteGenreResult = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val deleteFavoriteGenreResult: StateFlow<UiState<Unit>> = _deleteFavoriteGenreResult

    private val _incomingStreams = MutableStateFlow(listOf(false,false))
    val incomingStreams:StateFlow<List<Boolean>> = _incomingStreams

    private var id = 1

    fun addGenre(genreModel: GenreModel){
        viewModelScope.launch {
            addGenreUseCase.execute(
                AddGenreUseCase.Request(genreModel)
            ).map {
                addFavoriteGenreConverter.convert(it)
            }
                .collect {
                    when (it) {
                        is UiState.Error -> {
                            Log.e("feed_vm", it.errorMessage)
                        }

                        UiState.Loading -> {
                            Log.d("feed_vm", "loading")
                        }

                        is UiState.Success -> {
                            Log.e("feed_vm", it.data.toString() + "<--")

                        }
                    }
                }
        }
        getAllFavoriteGenres()
    }

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
                            Log.e("feed_vm",it.data.toString())
                            _incomingStreams.value = listOf( _incomingStreams.value[0],true)
                            _genres.value = it.data
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
                _deleteFavoriteGenreResult.value= it
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

    fun addFilmToFavorites(moviesModel: MovieElementModel){
        viewModelScope.launch {
            addFilmToFavoriteUseCase.execute(
                AddFavoriteMovieUseCase.Request(moviesModel.id)
            )
                .map {
                    addFavoriteMovieConverter.convert(it)
                }
                .collect{
                    _addFilmToFavoriteResult.value = it
                    when(it){
                        is UiState.Error -> {
                            Log.e("feed_vm",it.errorMessage)
                        }
                        UiState.Loading -> {
                            Log.d("feed_vm", "loading")
                        }
                        is UiState.Success -> {
                            Log.e("feed_vm",it.data.toString())

                        }
                    }
                }
        }
    }

    fun addFilmToDisliked(moviesModel: MovieElementModel){
        viewModelScope.launch {
            addFilmToDislikedUseCase.execute(
                AddFilmToDislikedUseCase.Request(
                    ShortMovieModel(moviesModel.id)
                )
            ).map{
                filmConverter.convert(it)
            }.collect{
                _addFilmToDislikedResult.value = it


            }
        }
    }

    fun loadAgain(){
        Log.d("feed_vm","yue")
        id++
        _incomingStreams.value = listOf(false,true)
        load()
    }

    fun load(){
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
                    when(it){
                        is UiState.Error -> {

                        }
                        UiState.Loading -> {

                        }
                        is UiState.Success -> {
                            _incomingStreams.value = listOf(true,_incomingStreams.value[1])
                        }
                    }
                }

        }
    }
}