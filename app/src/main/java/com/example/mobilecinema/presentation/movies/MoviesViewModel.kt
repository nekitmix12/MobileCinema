package com.example.mobilecinema.presentation.movies

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilecinema.R
import com.example.mobilecinema.data.StringHelper
import com.example.mobilecinema.data.model.movie.GenreModel
import com.example.mobilecinema.data.model.movie.MovieElementModel
import com.example.mobilecinema.data.model.movie.MoviesPagedListModel
import com.example.mobilecinema.domain.converters.MoviesRatingConverter
import com.example.mobilecinema.domain.converters.film.FavoriteMoviesConverter
import com.example.mobilecinema.domain.converters.film.MoviesConverter
import com.example.mobilecinema.domain.converters.genre.AddGenreToFavoriteConverter
import com.example.mobilecinema.domain.converters.genre.DeleteGenreFromFavoriteConverter
import com.example.mobilecinema.domain.converters.genre.GetGenresFromFavoriteConverter
import com.example.mobilecinema.domain.use_case.UiState
import com.example.mobilecinema.domain.use_case.favorite_movies_use_case.GetFavoriteMoviesUseCase
import com.example.mobilecinema.domain.use_case.genre.AddGenreUseCase
import com.example.mobilecinema.domain.use_case.genre.DeleteGenreUseCase
import com.example.mobilecinema.domain.use_case.genre.GetGenreUseCase
import com.example.mobilecinema.domain.use_case.movies_use_case.GetMoviesPageUseCase
import com.example.mobilecinema.domain.use_case.movies_use_case.MoviesRatingUseCase
import com.example.mobilecinema.presentation.adapter.Item
import com.example.mobilecinema.presentation.adapter.model.AllFilmModel
import com.example.mobilecinema.presentation.adapter.model.ButtonModel
import com.example.mobilecinema.presentation.adapter.model.CarouselModel
import com.example.mobilecinema.presentation.adapter.model.FavoriteModel
import com.example.mobilecinema.presentation.adapter.model.HeadingItem
import com.example.mobilecinema.presentation.adapter.model.HorizontalItem
import com.example.mobilecinema.presentation.adapter.model.HorizontalWithCarouselModel
import com.example.mobilecinema.presentation.adapter.model.LabelModel
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import kotlinx.coroutines.flow.MutableStateFlow
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
    private val deleteGenreUseCase: DeleteGenreUseCase,
    private val deleteGenreFromFavoriteConverter: DeleteGenreFromFavoriteConverter,
    private val addGenreUseCase: AddGenreUseCase,
    private val addFavoriteGenreConverter: AddGenreToFavoriteConverter,

    ) : ViewModel() {
    private val mutex = Mutex()
    private var listMutex = Mutex()

    private val _moviesRaw = MutableStateFlow<UiState<MoviesPagedListModel>>(UiState.Loading)

    private val _movies = MutableStateFlow<List<MovieElementModel>?>(null)
    private val _allFavoriteGenre = MutableStateFlow<UiState<List<GenreModel>>>(UiState.Loading)


    private var favoriteSet = setOf<String>()
    private var genreSet = setOf<GenreModel>()

    private var _error = MutableStateFlow<String?>(null)

    private var _carouselList = MutableStateFlow<List<CarouselModel>>(listOf())
    private var _isFavoriteUpdate = MutableStateFlow(false)

    private var isLoading = false
    private var isLastPage = false

    private val _loadingState = MutableStateFlow(false)

    private var _allMoviesList = MutableStateFlow<List<AllFilmModel>>(listOf())

    private var _favorite = MutableStateFlow<List<FavoriteModel>>(listOf())
    private val _moviesRating = MutableStateFlow<UiState<List<Float>>>(UiState.Loading)
    private val _show = MutableStateFlow(false)
    private val _feed = MutableStateFlow<List<Item>>(listOf())

    private var id = 1

    val error: StateFlow<String?> = _error
    val feed: StateFlow<List<Item>> = _feed

    init {
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
                combine(
                    _carouselList, _isFavoriteUpdate, _allMoviesList
                ) { carousel, favorite, all ->
                    listOf(
                        carousel.isNotEmpty(), favorite, all.isNotEmpty()
                    )
                }.collect { list ->
                    mutex.withLock {
                        if (list.all { it }) {
                            val tempFeed = mutableListOf<Item>()

                            tempFeed.addAll(createCarouselItems())
                            tempFeed.add(ButtonModel)
                            tempFeed.add(createFavoriteSection())
                            tempFeed.add(HorizontalItem(_favorite.value))
                            tempFeed.add(LabelModel(StringHelper().getString(R.string.all_film)))
                            tempFeed.addAll(createMovieItems().toSet().toList())

                            _feed.value = tempFeed

                        }
                    }
                }
            }
            launch {

                _moviesRating.collect {
                    if (it is UiState.Success && _movies.value != null) {
                        val list = mutableListOf<AllFilmModel>()
                        it.data.forEachIndexed { index, rating ->
                            list.add(
                                AllFilmModel(
                                    img = urlToBitmap(_movies.value!![index].poster),
                                    rating = rating,
                                    isFavorite = favoriteSet.contains(_movies.value!![index].id),
                                    movieId = _movies.value!![index].id
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

    private fun createCarouselItems(): List<Item> {
        return listOf(HorizontalWithCarouselModel(_carouselList.value))
    }

    private fun createFavoriteSection(): Item {
        return HeadingItem(
            StringHelper().getString(R.string.my_favorite),
            StringHelper().getString(R.string.all)
        )
    }

    private fun createMovieItems(): List<Item> =
        _allMoviesList.value.chunked(3).map { HorizontalItem(it) }


    fun buttonOnClick(movieId: String) {
        Log.d("genre", movieId.toString())

    }


    //загрузили фильмы
    fun loadMovies() {
        if (isLoading || isLastPage) return

        viewModelScope.launch {
            mutex.withLock {
                isLoading = true
            }

            getMoviesPageUseCase.execute(GetMoviesPageUseCase.Request(pageData = id))
                .map { moviesConverter.convert(it) }
                .collect { result ->
                    mutex.withLock {
                        if (result is UiState.Success && result.data.movies?.isNotEmpty() == true) {

                            _moviesRaw.value = result
                            isLoading = false
                            updateId()
                        }
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
                val temp = mutableListOf<CarouselModel>()
                Log.d("genre", "set is: $genreSet")
                _carouselList.value.forEach {
                    val tempList = mutableListOf<Pair<GenreModel, Boolean>>()
                    it.genres.forEach {
                        tempList.add(Pair(it.first, genreSet.contains(it.first)))
                        Log.d("genre", "set is: ${genreSet.contains(it.first)}")

                    }
                    temp.add(it.copy(genres = tempList))
                }
                _carouselList.value = temp
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
                if (it is UiState.Loading) Log.d("error", "loading")
                if (it is UiState.Success) {
                    Log.d("error", "${it.data.movies}")
                    val temp = mutableSetOf<String>()
                    val urls = mutableListOf<String>()

                    it.data.movies?.forEach { movie ->
                        temp.add(movie.id)
                        movie.poster?.let { it1 -> urls.add(it1) }
                    }

                    favoriteSet = temp

                    _favorite.value = urlToBitmap(urls)
                    Log.d("error", "_favorite2")
                    _isFavoriteUpdate.value = true
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
                mutex.withLock {
                    when (it) {
                        is UiState.Success -> {
                            if (_carouselList.value.size < 5) {

                                Log.d("sendasdasd", "movies start ${_carouselList.value.size}")

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
                                Log.d("sendasdasd", "movies end ${_carouselList.value.size}")
                                loadMovies()

                            } else {
                                it.data.movies?.forEach { movie ->
                                    _movies.value = _movies.value!!.plus(movie)

                                }
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

    private fun updateId() {
        id++
    }

    fun deleteFavoriteGenre(genreModel: GenreModel) {
        viewModelScope.launch {
            deleteGenreUseCase.execute(
                DeleteGenreUseCase.Request(
                    genreModel
                )
            ).map {
                deleteGenreFromFavoriteConverter.convert(it)
            }.collect {
                when (it) {
                    is UiState.Error -> {
                        Log.e("feed_vm", it.errorMessage)
                    }

                    UiState.Loading -> {
                        Log.d("feed_vm", "loading_all_genre")
                        getGenre()
                    }

                    is UiState.Success -> {
                        Log.d("fetch_data", it.data.toString() + "<_delete_>")
                        getGenre()
                    }
                }
                getGenre()
            }
        }
        getGenre()
    }

    fun addGenre(genreModel: GenreModel) {
        viewModelScope.launch {
            addGenreUseCase.execute(
                AddGenreUseCase.Request(genreModel)
            ).map {
                addFavoriteGenreConverter.convert(it)
            }.collect {
                when (it) {
                    is UiState.Error -> {
                        Log.e("feed_vm", it.errorMessage)
                    }

                    UiState.Loading -> {
                        Log.d("feed_vm", "loading")
                        getGenre()
                    }

                    is UiState.Success -> {
                        Log.e("feed_vm", it.data.toString() + "<--")
                        getGenre()
                    }
                }
            }
        }
        getGenre()
    }

    private fun loadAllFavoriteGenres() {
        viewModelScope.launch {
            getGenreUseCase.execute().map {
                getGenresFromFavoriteConverter.convert(it)
            }.collect {
                Log.i("fetch_data", "get new all genres")
                _allFavoriteGenre.value = it
            }
        }
    }
}



