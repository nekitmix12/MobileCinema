package com.example.mobilecinema.presentation.movies

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilecinema.data.model.favorite_movies.MoviesListModel
import com.example.mobilecinema.data.model.movie.GenreModel
import com.example.mobilecinema.data.model.movie.MovieElementModel
import com.example.mobilecinema.data.model.movie.MoviesPagedListModel
import com.example.mobilecinema.domain.converters.MoviesRatingConverter
import com.example.mobilecinema.domain.converters.film.FavoriteMoviesConverter
import com.example.mobilecinema.domain.converters.film.MoviesConverter
import com.example.mobilecinema.domain.converters.genre.GetGenresFromFavoriteConverter
import com.example.mobilecinema.domain.model.AllFilmModel
import com.example.mobilecinema.domain.use_case.UiState
import com.example.mobilecinema.domain.use_case.favorite_movies_use_case.GetFavoriteMoviesUseCase
import com.example.mobilecinema.domain.use_case.genre.GetGenreUseCase
import com.example.mobilecinema.domain.use_case.movies_use_case.GetMoviesPageUseCase
import com.example.mobilecinema.domain.use_case.movies_use_case.MoviesRatingUseCase
import com.example.mobilecinema.presentation.adapter.model.CarouselModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class MoviesViewModel(
    private val getMoviesPageUseCase: GetMoviesPageUseCase,
    private val moviesConverter: MoviesConverter,
    private val getGenreUseCase: GetGenreUseCase,
    private val getGenresFromFavoriteConverter: GetGenresFromFavoriteConverter,
    private val getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase,
    private val favoriteMoviesConverter: FavoriteMoviesConverter,
    private val moviesFilmRating: MoviesRatingUseCase,
    private val moviesRatingConverter: MoviesRatingConverter,
) : ViewModel() {

    private var listMutex = Mutex()

    private var _genreRaw = MutableStateFlow<UiState<List<GenreModel>>>(UiState.Loading)
    private val _moviesRaw = MutableStateFlow<UiState<MoviesPagedListModel>>(UiState.Loading)
    private val _moviesFavorite = MutableStateFlow<UiState<MoviesListModel>>(UiState.Loading)
    val moviesFavorite: StateFlow<UiState<MoviesListModel>> = _moviesFavorite

    private val _movies = MutableStateFlow<List<MovieElementModel>?>(null)


    private var genreSet = setOf<String>()
    private var favoriteSet = setOf<String>()


    private val _scrollAction = MutableSharedFlow<Unit>()
    val scrollAction: SharedFlow<Unit> = _scrollAction


    private var _allMoviesListLocal = MutableStateFlow(listOf<MovieElementModel>())

    private var _carouselList = MutableStateFlow(listOf<CarouselModel>())
    val carouselList: StateFlow<List<CarouselModel>> = _carouselList

    private var _allMoviesListFinish = MutableStateFlow(listOf<AllFilmModel>())
    val allMoviesList: StateFlow<List<AllFilmModel>> = _allMoviesListFinish


    private val _moviesRating = MutableStateFlow<UiState<List<Float>>>(UiState.Loading)
    val moviesRating: StateFlow<UiState<List<Float>>> = _moviesRating

    private var id = 0

    private var isGetCarouselItem = false
    private var haveElse = true

    //загрузили фильмы
    fun loadMovies() {
        if (!haveElse) return
        updateId()
        Log.d("send", "films")
        viewModelScope.launch {
            launch {
                getMoviesPageUseCase.execute(
                    GetMoviesPageUseCase.Request(
                        pageData = id
                    )
                ).map {
                    moviesConverter.convert(it)
                }

                    .collect {
                        _moviesRaw.value = it
                    }
            }
        }
    }

    //загрузили жанры
    fun getGenre() {
        Log.d("send", "genre")

        viewModelScope.launch {
            getGenreUseCase.execute(
                GetGenreUseCase.Request(
                )
            ).map {
                getGenresFromFavoriteConverter.convert(it)
            }.collect {
                _genreRaw.value = it
            }
        }
    }

    //загрузили любимые
    fun loadFavoritesMovies() {
        Log.d("send", "favorite")
        viewModelScope.launch {
            getFavoriteMoviesUseCase.execute().map {
                favoriteMoviesConverter.convert(it)
            }.collect {
                _moviesFavorite.value = it
            }
        }
    }

    //собрали фильмы и установили слушатель скролла и изменения фильмов
    fun collectAllFilms() {
        viewModelScope.launch {
            launch {
                combine(_moviesFavorite, _genreRaw) { first, second ->
                    first to second
                }.collect { (first, second) ->
                    Log.d(
                        "has error",
                        "favorite: ${first is UiState.Error} \n genre: ${if (second is UiState.Error) second.errorMessage else ""}"
                    )
                    if (first is UiState.Success && second is UiState.Success) {
                        Log.d("send", "collect")
                        genreToSet(second)
                        favoriteToSet(first)
                        collectMovies()
                        setMovieObserver()
                        ratingObserver()
                    }
                }
            }

        }
    }

    private fun collectMovies() {
        viewModelScope.launch {
            _moviesRaw.collect {
                when (it) {
                    is UiState.Success -> {
                        if (_carouselList.value.size < 6) {

                            Log.d("send", "movies raw")

                            val listPairs = mutableListOf<CarouselModel>()

                            it.data.movies?.forEachIndexed { index, movie ->
                                Log.d("iter", "$index <-> $movie")
                                if (index < 5) {

                                    val tempCarouseModel = CarouselModel(
                                        movie.id,
                                        movie.moveName,
                                        movie.poster,
                                        mutableListOf<Pair<String, Boolean>>()
                                    )
                                    movie.genres.forEach { genre ->
                                        tempCarouseModel.genres = tempCarouseModel.genres.plus(
                                            Pair(
                                                genre?.genreName ?: "", genreSet.contains(genre?.id)
                                            )
                                        )
                                    }
                                    Log.d("iter", "$listPairs")
                                    listPairs.add(tempCarouseModel)
                                } else {
                                    if (_movies.value != null) _movies.value =
                                        _movies.value!!.plus(movie)
                                    else _movies.value = listOf(movie)
                                }
                            }
                            Log.d("iter", "$listPairs")
                            _carouselList.value = listPairs
                        }
                    }

                    is UiState.Error -> {
                        Log.d("send", "movies raw error ${it.errorMessage}")
                    }

                    is UiState.Loading -> {}
                }
            }
        }
    }

    private fun genreToSet(genreResult: UiState.Success<List<GenreModel>>) {
        Log.d("send", "genre to set")
        val setGenres = mutableSetOf<String>()
        genreResult.data.forEach { genre ->
            setGenres.add(genre.id)
        }
        genreSet = setGenres
    }

    private fun favoriteToSet(favoriteResult: UiState.Success<MoviesListModel>) {
        Log.d("send", "favorite to set")
        val setFavorites = mutableSetOf<String>()
        favoriteResult.data.movies?.forEach { favorite ->
            setFavorites.add(favorite.id)
        }
        favoriteSet = setFavorites
    }

    private fun setMovieObserver() {
        viewModelScope.launch {
            _movies.collect {
                if (it == null) return@collect
                Log.d("update", "movies")
                val tempList = listOf<MovieElementModel>()
                for (i in 0..<it.size / 3 * 3) tempList.plus(it[i])
                loadRatings(tempList)
            }
        }
    }


    private fun loadRatings(movies: List<MovieElementModel>) {
        viewModelScope.launch {
            listMutex.withLock {
                moviesFilmRating.execute(
                    MoviesRatingUseCase.Request(
                        movies = movies
                    )
                ).map {
                    moviesRatingConverter.convert(it)
                }.collect {
                    _moviesRating.value = it
                    Log.d("get", "rating")

                }
            }
        }
    }

    private fun ratingObserver() {
        viewModelScope.launch {
            _moviesRating.collect {
                if (it is UiState.Success && _movies.value != null) {
                    val list = mutableListOf<AllFilmModel>()
                    it.data.forEachIndexed { index, rating ->
                        list.add(
                            AllFilmModel(
                                url = _movies.value!![index].poster,
                                rating = rating,
                                isLiked = favoriteSet.contains(_movies.value!![index].id),
                            )
                        )
                    }
                    Log.d("update", "rating")
                    _allMoviesListFinish.value = list
                }
            }
        }
    }

    private fun updateId() {
        id++
    }
}



