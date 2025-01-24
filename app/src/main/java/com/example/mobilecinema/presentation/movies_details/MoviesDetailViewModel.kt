package com.example.mobilecinema.presentation.movies_details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilecinema.R
import com.example.mobilecinema.data.model.auth.ProfileDTO
import com.example.mobilecinema.data.model.favorite_movies.MoviesListModel
import com.example.mobilecinema.data.model.kinopoisk_api.Country
import com.example.mobilecinema.data.model.kinopoisk_api.Film
import com.example.mobilecinema.data.model.kinopoisk_api.SearchFilmResponse
import com.example.mobilecinema.data.model.movie.GenreModel
import com.example.mobilecinema.data.model.movie.MovieDetailsModel
import com.example.mobilecinema.data.model.movie.ShortMovieModel
import com.example.mobilecinema.data.model.review.ReviewModel
import com.example.mobilecinema.data.model.review.ReviewModifyModel
import com.example.mobilecinema.di.MainContext
import com.example.mobilecinema.domain.converters.AddFilmToDislikedConverter
import com.example.mobilecinema.domain.converters.DeleteFilmFromDislikedConverter
import com.example.mobilecinema.domain.converters.FilmConverter
import com.example.mobilecinema.domain.converters.MovieDetailsConverter
import com.example.mobilecinema.domain.converters.ProfileConverter
import com.example.mobilecinema.domain.converters.SearchFilmConverter
import com.example.mobilecinema.domain.converters.film.AddFilmToFavoriteConverter
import com.example.mobilecinema.domain.converters.film.DeleteFilmFromFavoriteConverter
import com.example.mobilecinema.domain.converters.film.FavoriteMoviesConverter
import com.example.mobilecinema.domain.converters.genre.AddGenreToFavoriteConverter
import com.example.mobilecinema.domain.converters.genre.DeleteGenreFromFavoriteConverter
import com.example.mobilecinema.domain.converters.genre.GetGenresFromFavoriteConverter
import com.example.mobilecinema.domain.converters.review.AddReviewConverter
import com.example.mobilecinema.domain.converters.review.DeleteReviewConverter
import com.example.mobilecinema.domain.converters.review.EditReviewConverter
import com.example.mobilecinema.domain.use_case.UiState
import com.example.mobilecinema.domain.use_case.favorite_movies_use_case.AddFavoriteMovieUseCase
import com.example.mobilecinema.domain.use_case.favorite_movies_use_case.DeleteFavoriteMovieUseCase
import com.example.mobilecinema.domain.use_case.favorite_movies_use_case.GetFavoriteMoviesUseCase
import com.example.mobilecinema.domain.use_case.genre.AddGenreUseCase
import com.example.mobilecinema.domain.use_case.genre.DeleteGenreUseCase
import com.example.mobilecinema.domain.use_case.genre.GetGenreUseCase
import com.example.mobilecinema.domain.use_case.kinopoisk_use_case.GetFilmUseCase
import com.example.mobilecinema.domain.use_case.kinopoisk_use_case.SearchFilmUseCase
import com.example.mobilecinema.domain.use_case.movies_use_case.AddFilmToDislikedUseCase
import com.example.mobilecinema.domain.use_case.movies_use_case.DeleteFilmFromDislikedUseCase
import com.example.mobilecinema.domain.use_case.movies_use_case.GetMoviesDetailsUseCase
import com.example.mobilecinema.domain.use_case.review_use_case.AddReviewUseCase
import com.example.mobilecinema.domain.use_case.review_use_case.DeleteReviewUseCase
import com.example.mobilecinema.domain.use_case.review_use_case.EditReviewUseCase
import com.example.mobilecinema.domain.use_case.user_use_case.GetProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.roundToInt

class MoviesDetailViewModel(
    private val addReviewUseCase: AddReviewUseCase,
    private val editReviewUseCase: EditReviewUseCase,
    private val deleteReviewUseCase: DeleteReviewUseCase,
    private val getFilmUseCase: GetFilmUseCase,
    private val searchFilmUseCase: SearchFilmUseCase,
    private val searchFilmConverter: SearchFilmConverter,
    private val getMoviesDetailsUseCase: GetMoviesDetailsUseCase,
    private val movieDetailsConverter: MovieDetailsConverter,
    private val filmConverter: FilmConverter,
    private val addFilmToDislikedUseCase: AddFilmToDislikedUseCase,
    private val addFilmToDislikedConverter: AddFilmToDislikedConverter,
    private val addFilmToFavoriteUseCase: AddFavoriteMovieUseCase,
    private val addFavoriteMovieConverter: AddFilmToFavoriteConverter,
    private val addGenreUseCase: AddGenreUseCase,
    private val addFavoriteGenreConverter: AddGenreToFavoriteConverter,
    private val getGenreUseCase: GetGenreUseCase,
    private val getGenresFromFavoriteConverter: GetGenresFromFavoriteConverter,
    private val deleteGenreUseCase: DeleteGenreUseCase,
    private val deleteGenreFromFavoriteConverter: DeleteGenreFromFavoriteConverter,
    private val getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase,
    private val getFavoriteMoviesConverter: FavoriteMoviesConverter,
    private val deleteFavoriteMovieUseCase: DeleteFavoriteMovieUseCase,
    private val deleteFavoriteMoviesConverter: DeleteFilmFromFavoriteConverter,
    private val addReviewConverter: AddReviewConverter = AddReviewConverter(),
    private val editReviewConverter: EditReviewConverter = EditReviewConverter(),
    private val deleteReviewConverter: DeleteReviewConverter = DeleteReviewConverter(),
    private val deleteFilmFromDislikedUseCase: DeleteFilmFromDislikedUseCase = DeleteFilmFromDislikedUseCase(),
    private val deleteFilmFromDislikedConverter: DeleteFilmFromDislikedConverter = DeleteFilmFromDislikedConverter(),
    private val getProfileUseCase: GetProfileUseCase = GetProfileUseCase(),
    private val getProfileConverter: ProfileConverter = ProfileConverter(),
) : ViewModel() {

    var id = ""

    private var profile = MutableStateFlow<UiState<ProfileDTO>>(UiState.Loading)
    private var _hasComment = MutableStateFlow<UiState<ReviewModel>>(UiState.Loading)

    private var _isLoaded = MutableStateFlow(false)
    val isLoaded: StateFlow<Boolean> = _isLoaded

    private val _favoriteMovies = MutableStateFlow<UiState<MoviesListModel>>(UiState.Loading)
    private var _isFavorite = MutableStateFlow<Boolean?>(null)

    private val _allFavoriteGenre = MutableStateFlow<UiState<List<GenreModel>>>(UiState.Loading)
    private val _allGenres = MutableStateFlow<List<GenreModel?>?>(null)
    private var _genres = MutableStateFlow<List<Pair<GenreModel, Boolean>>?>(null)
    private var _reviews = MutableStateFlow<List<ReviewModel>?>(null)

    private val _details = MutableStateFlow<UiState<MovieDetailsModel>>(UiState.Loading)
    private val _detailsObservable = MutableStateFlow<MovieDetailsModel?>(null)

    private val _searchResult = MutableStateFlow<UiState<SearchFilmResponse>>(UiState.Loading)

    private val _film = MutableStateFlow<UiState<Film>>(UiState.Loading)
    private val _filmObservable = MutableStateFlow<Film?>(null)

    private val _error = MutableStateFlow<String?>(null)

    private val _rating = MutableStateFlow<UiState<Float>>(UiState.Loading)
    private val _ratingResult = MutableStateFlow<Float?>(null)

    //отслеживаемые
    val hasComment: StateFlow<UiState<ReviewModel>> = _hasComment
    val genres: StateFlow<List<Pair<GenreModel, Boolean>>?> = _genres
    val reviews: StateFlow<List<ReviewModel>?> = _reviews
    val details: StateFlow<MovieDetailsModel?> = _detailsObservable
    val film: StateFlow<Film?> = _filmObservable
    val rating: StateFlow<Float?> = _ratingResult
    val error: StateFlow<String?> = _error
    val isFavorite: StateFlow<Boolean?> = _isFavorite


    //детали, для них жанры,любимые фильмы для экрана,друзей, респонс поиска и от него уже фильмы
    private fun collectorMain() {
        viewModelScope.launch {
            val flows = listOf(hasComment, genres, reviews, details, film, rating, isFavorite)
            combine(flows) { states ->
                listOf(
                    (states[0] as UiState<*>).let { it !is UiState.Loading },
                    states[1] != null,
                    states[2] != null,
                    states[3] != null,
                    states[4] != null,
                    states[5] != null,
                    states[6] != null
                )
            }.collect { states ->
                Log.d("stateIChanges", "$states")

                if (states.all { it }) {

                    _isLoaded.value = true
                }
            }
        }

    }

    fun init(
        filmId: String,
    ) {
        id = filmId
        collectorMain()
        collectorsMain(filmId)
        loadDetails(filmId)
        loadAllFavoriteGenres()
        loadFriends()
        getFavoritesFilms()
        getProfile()
    }

    fun setErrorNull() {
        _error.value = null
    }

    private fun collectorsMain(filmId: String) {
        viewModelScope.launch {
            launch {
                combine(_allGenres, _allFavoriteGenre) { genres, favoriteGenres ->
                    genres to favoriteGenres
                }.collect { (genresAll, favoriteGenres) ->
                    if (genresAll != null
                        && favoriteGenres is UiState.Success<List<GenreModel>>
                    ) {
                        Log.d("fetch_data", "collect an mutable data")
                        _genres.value = mapToGenre(
                            genresAll,
                            favoriteGenres.data.toSet()
                        )
                    }
                }
                _allFavoriteGenre.collect {
                    when (it) {
                        is UiState.Error -> {
                            Log.d("Movies_details", "_allFavoriteGenre ${it.errorMessage}")
                        }

                        is UiState.Success -> {
                            if (_details.value is UiState.Success) {
                                Log.i("fetch_data", "_allFavoriteGenre")
                                _genres.value = mapToGenre(
                                    (_details.value as UiState.Success).data.genres,
                                    (it.data).toSet()
                                )
                            }

                        }

                        is UiState.Loading -> {}
                    }
                }
            }
            launch {
                _favoriteMovies.collect { it ->
                    when (it) {
                        is UiState.Error -> {
                            Log.d("Movies_details", "_favoriteMovies ${it.errorMessage}")
                        }

                        is UiState.Success -> {
                            _isFavorite.value = it.data.movies?.any { it.id == filmId } ?: false
                        }

                        is UiState.Loading -> {}
                    }
                }
            }
            launch {
                _details.collect {
                    when (it) {
                        is UiState.Error -> {
                            Log.d("Movies_details", "_details ${it.errorMessage}")
                        }

                        is UiState.Success -> {
                            if (_allGenres.value == null)
                                _allGenres.value = it.data.genres
                            if (_reviews.value == null)
                                _reviews.value = it.data.reviews

                            _detailsObservable.value = it.data

                            it.data.name?.let { it1 ->
                                searchFilm(it1)
                            }
                            val rating = getRating(it.data.reviews)
                            _rating.value = UiState.Success(rating)
                            _ratingResult.value = rating
                        }

                        is UiState.Loading -> {}
                    }
                }
            }
            launch {
                _searchResult.collect {
                    when (it) {
                        is UiState.Error -> {
                            Log.d("Movies_details", "_searchResult error: ${it.errorMessage}")
                        }

                        is UiState.Success -> {
                            if (_details.value is UiState.Success) {
                                Log.d("Movies_details", "_searchResult success: ${it.data}")
                                getFilmById(
                                    findIdFromFilm(
                                        it.data,
                                        (_details.value as UiState.Success<MovieDetailsModel>).data
                                    )
                                )
                            }
                        }

                        is UiState.Loading -> {}
                    }
                }
            }

            launch {
                combine(profile, _details) { (profileIt, details) ->
                    profileIt to details
                }.collect {
                    Log.d("common_logger","changed profile or _details")
                    if (profile.value is UiState.Success<ProfileDTO> && _details.value is UiState.Success<MovieDetailsModel>) {
                        var comment: ReviewModel? = null
                        if ((_details.value as UiState.Success<MovieDetailsModel>).data.reviews != null) {
                            Log.d("common_logger","details:"+(_details.value as UiState.Success<MovieDetailsModel>).data.reviews.toString())
                            Log.d("common_logger","profile:"+(profile.value as UiState.Success<ProfileDTO>).data.id)
                            for (i in (_details.value as UiState.Success<MovieDetailsModel>).data.reviews!!)
                                if (i.author?.userId == (profile.value as UiState.Success<ProfileDTO>).data.id)
                                    comment = i
                        }
                        _hasComment.value =
                            if (comment == null) UiState.Error("") else UiState.Success(
                                ReviewModel(
                                    id = comment.id,
                                    createDateTime = comment.createDateTime,
                                    author = comment.author,
                                    reviewText = comment.reviewText ?: "",
                                    rating = comment.rating,
                                    isAnonymous = comment.isAnonymous
                                )
                            )
                    }
                }
            }
            launch {
                _film.collect {
                    when (it) {
                        is UiState.Loading -> {
                        }

                        is UiState.Success -> {
                            _filmObservable.value = it.data
                        }

                        is UiState.Error -> {
                            Log.e("fetch_error", "film error: ${it.errorMessage}")
                        }
                    }
                }
            }
        }
    }

    private fun mapToGenre(
        genres: List<GenreModel?>,
        genreSet: Set<GenreModel>,
    ): List<Pair<GenreModel, Boolean>> {
        val result = mutableListOf<Pair<GenreModel, Boolean>>()
        genres.forEach {
            if (it != null) result.add(Pair(it, genreSet.contains(it)))
        }
        return result
    }

    fun loadDetails(id: String = this.id) {
        Log.d("loadDetails", id)
        viewModelScope.launch {
            getMoviesDetailsUseCase.execute(
                GetMoviesDetailsUseCase.Request(
                    id
                )
            ).map {
                movieDetailsConverter.convert(it)
            }.collect {
                _details.value = correctedTime(it)
            }
        }
    }

    private fun correctedTime(dm: UiState<MovieDetailsModel>): UiState<MovieDetailsModel> {
        if (dm is UiState.Success) {
            val newDm = mutableListOf<ReviewModel>()
            dm.data.reviews?.forEach {
                newDm.add(it.copy(createDateTime = isoToReadableDate(it.createDateTime)))
            }
            return UiState.Success(dm.data.copy(reviews = newDm))
        } else return dm
    }

    private fun isoToReadableDate(isoTime: String): String {
        val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        isoFormat.timeZone = TimeZone.getTimeZone("UTC")

        val isoFormatWithZ = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        isoFormatWithZ.timeZone = TimeZone.getTimeZone("UTC")

        val readableFormat = SimpleDateFormat("d MMMM yyyy", Locale("ru"))
        val date: Date = try {
            isoFormat.parse(isoTime)!!
        } catch (e: Exception) {
            try {
                isoFormatWithZ.parse(isoTime)!!
            } catch (e: Exception) {
                Log.e("exception", e.message.toString())
                return "Некорректная дата"
            }
        }

        return date.let { readableFormat.format(it) } ?: "Некорректная дата"
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

    private fun loadFriends() {
        Log.d("Movies_details", "loadFriends")

    }

    fun toStingCountry(counties: List<Country>): String {
        var resultString = ""

        for (i in counties.indices) {
            if (i == 0) {
                resultString += counties[i].country
                continue
            }
            resultString += ", ${counties[i].country}"

        }
        return resultString
    }

    fun getFavoritesFilms() {
        viewModelScope.launch {
            getFavoriteMoviesUseCase.execute().map {
                getFavoriteMoviesConverter.convert(it)
            }.collect {
                Log.d("Movies_details", "getFavoritesFilms")

                _favoriteMovies.value = it
            }
        }
    }

    private fun searchFilm(name: String) {
        viewModelScope.launch {
            Log.d("levels", "0")
            searchFilmUseCase.execute(
                SearchFilmUseCase.Request(
                    name, 1
                )
            ).map {
                Log.d("convert", it.toString())
                searchFilmConverter.convert(it)
            }.collect {
                _searchResult.value = it
            }
        }
    }

    private fun getFilmById(id: Int) {
        viewModelScope.launch {
            getFilmUseCase.execute(
                GetFilmUseCase.Request(id)
            ).map {
                filmConverter.convert(it)
            }.collect {
                Log.d("Movies_details", "getFilmById")

                _film.value = it
            }
        }
    }

    fun deleteFromFavorite() {
        viewModelScope.launch {
            launch {
                addFilmToDisliked()
            }
            launch {
                deleteFavoriteMovieUseCase.execute(
                    DeleteFavoriteMovieUseCase.Request(id)
                ).map {
                    deleteFavoriteMoviesConverter.convert(it)
                }.collect { el ->
                    when (el) {
                        is UiState.Error -> {
                            Log.d("movies_details", el.errorMessage)
                        }

                        UiState.Loading -> {
                            Log.d("movies_details", "loading")
                        }

                        is UiState.Success -> {
                            _isFavorite.value = false
                            Log.d("movies_details", el.data.toString())
                        }
                    }
                }
            }
        }
    }

    fun timeConvert(time: Int?): String {
        if (time == null) return getString(R.string.not_time)
        var minutes = time
        var hours = 0
        while (minutes > 59) {
            minutes -= 60
            hours++
        }
        return "$hours ${getString(R.string.hours)} $minutes ${getString(R.string.minutes)}"
    }

    private fun getString(res: Int): String {
        return MainContext.provideInstance().provideContext().getString(res)
    }

    fun addReview(reviewModel: ReviewModifyModel, movieId: String) {
        viewModelScope.launch {
            Log.d("review", "$movieId <--->>  $reviewModel")
            addReviewUseCase.execute(AddReviewUseCase.Request(movieId, reviewModel)).map {
                addReviewConverter.convert(it)
            }.collect {
                if (it is UiState.Error) _error.value = it.errorMessage
                loadDetails()
            }
        }
    }


    fun editReview(reviewModel: ReviewModifyModel) {
        viewModelScope.launch {
            if (_hasComment.value is UiState.Success<ReviewModel>)
                editReviewUseCase.execute(
                    EditReviewUseCase.Request(
                        id,
                        (_hasComment.value as UiState.Success<ReviewModel>).data.id, reviewModel
                    )
                ).map {
                    editReviewConverter.convert(it)
                }.collect {
                    if (it is UiState.Error) Log.e("error_logger",it.errorMessage)
                    if (it is UiState.Error) _error.value = it.errorMessage
                }
        }
    }


    fun deleteReview(reviewId: String) {
        viewModelScope.launch {
            _hasComment.value = UiState.Error("")
            deleteReviewUseCase.execute(DeleteReviewUseCase.Request(id,reviewId)).map {
                deleteReviewConverter.convert(it)
            }.collect {
                if (it is UiState.Error) Log.e("error_logger",it.errorMessage)
                if (it is UiState.Error) _error.value = it.errorMessage
            }
        }
    }


    private fun findIdFromFilm(
        searchFilmDto: SearchFilmResponse,
        moviesDetailsModel: MovieDetailsModel,
    ): Int {
        var id: Int = searchFilmDto.films[0].filmId
        for (element in searchFilmDto.films) {
            if (element.year == moviesDetailsModel.year.toString()) id = element.filmId
        }
        return id
    }


    private fun getRating(reviews: List<ReviewModel>?): Float {

        if (reviews != null) {
            var rating = 0f
            for (review in reviews) rating += review.rating.toFloat()

            return (rating / reviews.size * 10).roundToInt() / 10f
        }
        return 0f
    }

    private fun addFilmToDisliked() {
        viewModelScope.launch {
            addFilmToDislikedUseCase.execute(
                AddFilmToDislikedUseCase.Request(
                    ShortMovieModel(id)
                )
            ).map {
                addFilmToDislikedConverter.convert(it)
            }.collect {
                Log.d("common_logger", "film add to disliked")

            }
        }
    }

    private fun deleteFilmToDisliked() {
        viewModelScope.launch {
            deleteFilmFromDislikedUseCase.execute(
                DeleteFilmFromDislikedUseCase.Request(
                    ShortMovieModel(id)
                )
            ).map {
                deleteFilmFromDislikedConverter.convert(it)
            }.collect {
                Log.d("common_logger", "film delete from disliked")

            }
        }
    }

    fun addFilmToFavorites() {
        viewModelScope.launch {
            launch {
                deleteFilmToDisliked()
            }
            launch {
                addFilmToFavoriteUseCase.execute(
                    AddFavoriteMovieUseCase.Request(id)
                ).map {
                    addFavoriteMovieConverter.convert(it)
                }.collect {

                    when (it) {
                        is UiState.Error -> {
                            Log.e("feed_vm", it.errorMessage)
                        }

                        UiState.Loading -> {
                            Log.d("feed_vm", "loading")
                        }

                        is UiState.Success -> {
                            _isFavorite.value = true
                            Log.e("feed_vm", it.data.toString())

                        }
                    }
                }
            }
        }
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
                        loadAllFavoriteGenres()
                    }

                    is UiState.Success -> {
                        Log.d("fetch_data", it.data.toString() + "<_delete_>")
                        loadAllFavoriteGenres()
                    }
                }
                loadAllFavoriteGenres()
            }
        }
        loadAllFavoriteGenres()
    }

    private fun getProfile() {
        viewModelScope.launch {
            getProfileUseCase.execute(
                GetProfileUseCase.Request()
            ).map {
                getProfileConverter.convert(it)
            }.collect {
                profile.value = it
            }
        }
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
                        loadAllFavoriteGenres()
                    }

                    is UiState.Success -> {
                        Log.e("feed_vm", it.data.toString() + "<--")
                        loadAllFavoriteGenres()
                    }
                }
            }
        }
        loadAllFavoriteGenres()
    }
}