package com.example.mobilecinema.presentation.movies

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
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
import com.example.mobilecinema.presentation.adapter.Item
import com.example.mobilecinema.presentation.adapter.delegates.CarouselDelegate
import com.example.mobilecinema.presentation.adapter.delegates.FavoriteDelegate
import com.example.mobilecinema.presentation.adapter.delegates.HorizontalItemDelegates
import com.example.mobilecinema.presentation.adapter.model.CarouselModel
import com.example.mobilecinema.presentation.adapter.model.FavoriteModel
import com.example.mobilecinema.presentation.adapter.model.HorizontalItem
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
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

    private val _movies = MutableStateFlow<List<MovieElementModel>?>(null)


    private var favoriteSet = setOf<String>()
    private var genreSet = setOf<GenreModel>()

    private val _scrollAction = MutableSharedFlow<Unit>()
    val scrollAction: SharedFlow<Unit> = _scrollAction

    private var _error = MutableStateFlow<String?>(null)
    private var _allMoviesListLocal = MutableStateFlow(listOf<MovieElementModel>())

    private var _carouselList = MutableStateFlow<List<CarouselModel>>(listOf())

    private var _allMoviesList = MutableStateFlow<List<AllFilmModel>?>(null)
   // private var _favorite = MutableStateFlow<List<FavoriteModel>?>(null)
    private var _favorite = MutableStateFlow<List<FavoriteModel>>(listOf())
    private val _moviesRating = MutableStateFlow<UiState<List<Float>>>(UiState.Loading)
    private val _show = MutableStateFlow(false)
    private val _feed = MutableStateFlow<List<Item>>(listOf())

    private var id = 0

    //отслеживаемые
    /*val carouselList: StateFlow<List<CarouselModel>> = _carouselList
    val favorite: StateFlow<List<FavoriteModel>?> = _favorite
    val allMoviesList: StateFlow<List<AllFilmModel>?> = _allMoviesList*/
    val error: StateFlow<String?> = _error
    val feed: StateFlow<List<Item>> =  _feed

    fun init() {
        viewModelScope.launch {
            launch { loadMovies() }
            launch { getGenre() }
            launch { loadFavoritesMovies() }
            launch { collectMovies() }
            launch { setMovieObserver() }
            launch { mainCollector() }
        }
    }

    private fun mainCollector() {
        viewModelScope.launch {
            launch {
                combine(_carouselList, _favorite, _allMoviesList) { carousel, favorite, all ->

                    listOf(
                        carousel.isNotEmpty(), favorite.isNotEmpty(), all != null
                    )
                }.collect { list ->
                    Log.d("main_collector",list.toString())
                    if (list.all { it }) {
                        val tempFeed = mutableListOf<Item>()
                        listOf(
                            CarouselDelegate(genreOnClick = ::genreOnClick,
                                buttonOnClick = ::buttonOnClick),
                            HorizontalItemDelegates(
                                listOf(
                                    FavoriteDelegate()
                                ),
                                70
                            ),
                            HorizontalItemDelegates(
                                listOf(
                                    CarouselDelegate(::genreOnClick,::buttonOnClick)
                                ),
                                70
                            )
                        ).forEach {
                            Log.d("loggerrer",it.isRelativeItem(HorizontalItem(_carouselList.value)).toString())
                        }
                        Log.d("main_collector","${HorizontalItem(_carouselList.value) }")
                        tempFeed.add(HorizontalItem(_carouselList.value))
                        tempFeed.add(HorizontalItem(_favorite.value))

                        //tempFeed.add(_allMoviesList.value)
                        _feed.value = tempFeed
                    }
                }
            }
            launch {
                viewModelScope.launch {
                    _moviesRating.collect {
                        if (it is UiState.Success && _movies.value != null) {
                            val list = mutableListOf<AllFilmModel>()
                            it.data.forEachIndexed { index, rating ->
                                list.add(
                                    AllFilmModel(
                                        url = urlToBitmap(_movies.value!![index].poster),
                                        rating = rating,
                                        isLiked = favoriteSet.contains(_movies.value!![index].id),
                                    )
                                )
                            }
                            Log.d("update", "rating")
                            _allMoviesList.value = list
                        }
                    }
                }
            }
        }
    }
    private fun genreOnClick(genre:GenreModel){
        Log.d("genre",genre.toString())
    }

    private fun buttonOnClick(movieId:String){
        Log.d("genre",movieId.toString())

    }

    private var haveElse = true

    //загрузили фильмы
    private fun loadMovies() {
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
                }.collect {
                    _moviesRaw.value = it
                }
            }
        }
    }

    //загрузили жанры
    private fun getGenre() {
        viewModelScope.launch {
            getGenreUseCase.execute(
                GetGenreUseCase.Request(
                )
            ).map {
                getGenresFromFavoriteConverter.convert(it)
            }.collect {
                if (it is UiState.Success) genreSet = it.data.toSet()
                if (it is UiState.Error) _error.value = it.errorMessage
            }
        }
    }

    //загрузили любимые
    private fun loadFavoritesMovies() {
        Log.d("send", "favorite")
        viewModelScope.launch {
            getFavoriteMoviesUseCase.execute().map {
                favoriteMoviesConverter.convert(it)
            }.collect {
                if (it is UiState.Success) {
                    val temp = mutableSetOf<String>()
                    val urls = mutableListOf<String>()

                    it.data.movies?.forEach { movie ->
                        temp.add(movie.id)
                        movie.poster?.let { it1 -> urls.add(it1) }
                    }

                    favoriteSet = temp

                    _favorite.value = urlToBitmap(urls)
                }
                if (it is UiState.Error) _error.value = it.errorMessage
            }
        }
    }

    private fun urlToBitmap(urls: List<String>): List<FavoriteModel> {
        val result = mutableListOf<FavoriteModel>()
        urls.forEach {
            Picasso.get().load(it).into(object : Target {
                override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
                    result.add(FavoriteModel(bitmap))
                }

                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {}

                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
            })
        }
        return result
    }

    private fun urlToBitmap(urls: String?): Bitmap? {
        var result: Bitmap? = null

        Picasso.get().load(urls).into(object : Target {
            override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
                result = bitmap
            }

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {}

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
        })

        return result
    }

    private fun collectMovies() {
        viewModelScope.launch {
            _moviesRaw.collect {
                when (it) {
                    is UiState.Success -> {
                        if (_carouselList.value.size<6) {

                            Log.d("send", "movies raw")

                            val listPairs = mutableListOf<CarouselModel>()

                            it.data.movies?.forEachIndexed { index, movie ->
                                Log.d("iter", "$index <-> $movie")
                                if (index < 5) {

                                    val tempCarouseModel = CarouselModel(
                                        movie.id,
                                        movie.moveName,
                                        urlToBitmap(movie.poster),
                                        mutableListOf()
                                    )

                                    movie.genres.forEach { genre ->
                                        if (genre != null) tempCarouseModel.genres =
                                            tempCarouseModel.genres.plus(
                                                Pair(
                                                    genre, genreSet.contains(genre)
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
                            Log.d("iter", "is iter: $listPairs")
                            _carouselList.value = listPairs
                            Log.d("iter", "is iter: ${_carouselList.value}")

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



    private fun setMovieObserver() {
        viewModelScope.launch {
            _movies.collect {
                if (it == null) return@collect
                Log.d("update", "movies")
                var tempList = listOf<MovieElementModel>()
                for (i in 0..<it.size / 3 * 3) tempList = tempList.plus(it[i])
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

    }

    private fun updateId() {
        id++
    }
}



