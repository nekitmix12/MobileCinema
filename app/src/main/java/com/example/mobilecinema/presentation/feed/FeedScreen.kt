package com.example.mobilecinema.presentation.feed

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.mobilecinema.R
import com.example.mobilecinema.data.datasource.local.AppDataBase
import com.example.mobilecinema.data.datasource.local.TokenStorageDataSourceImpl
import com.example.mobilecinema.data.datasource.local.data_source.FilmLocalDataSourceImpl
import com.example.mobilecinema.data.datasource.local.data_source.GenreLocalDataSourceImpl
import com.example.mobilecinema.data.datasource.remote.data_source.implementation.FavoritesMoviesRemoteDataSourceImpl
import com.example.mobilecinema.data.datasource.remote.data_source.implementation.MoviesRemoteDataSourceImpl
import com.example.mobilecinema.data.model.movie.GenreModel
import com.example.mobilecinema.data.model.movie.MovieElementModel
import com.example.mobilecinema.data.model.movie.MoviesPagedListModel
import com.example.mobilecinema.data.network.AuthInterceptor
import com.example.mobilecinema.data.network.NetworkModule
import com.example.mobilecinema.data.repository.FavoriteMoviesRepositoryImpl
import com.example.mobilecinema.data.repository.GenreRepositoryImpl
import com.example.mobilecinema.data.repository.MoviesRepositoryImpl
import com.example.mobilecinema.databinding.FeedScreenBinding
import com.example.mobilecinema.domain.UseCase
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
import com.example.mobilecinema.presentation.movies_details.MoviesDetailsActivity
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.CardStackView
import com.yuyakaido.android.cardstackview.Direction
import com.yuyakaido.android.cardstackview.StackFrom
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//я знаю что проверку нужно делать в domain слое но у меня не хватило времени довести до идеала
class FeedScreen : Fragment(R.layout.feed_screen) {
    private lateinit var cardStackView: CardStackView
    private lateinit var manager: CardStackLayoutManager
    private lateinit var adapter: CardStackAdapter
    private lateinit var viewModel: FeedViewModel
    private lateinit var moviesPagedListModel: MoviesPagedListModel
    private lateinit var genreModels: List<GenreModel>
    private var binding: FeedScreenBinding? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FeedScreenBinding.bind(view)

        val sharedPreferences = requireContext().getSharedPreferences(
            requireContext().getString(R.string.preference_is_logged_in), Context.MODE_PRIVATE
        )
        val dataBase = Room.databaseBuilder(
            requireContext(), AppDataBase::class.java, "app_database"
        ).build()
        sharedPreferences.getString("authToken", "")?.let { Log.d("feed_screen", it) }

        val tokenStorage = TokenStorageDataSourceImpl(sharedPreferences)
        val networkModule = NetworkModule()
        val interceptor = AuthInterceptor(tokenStorage)
        val apiServiceMovies = networkModule.provideMovieService(
            networkModule.provideRetrofit(
                networkModule.provideOkHttpClient(interceptor)
            )
        )
        val apiServiceFavoritesMovies = networkModule.provideFavoriteMoviesService(
            networkModule.provideRetrofit(
                networkModule.provideOkHttpClient(interceptor)
            )
        )
        val filmLocalDataSource = FilmLocalDataSourceImpl(dataBase.filmDao())
        val moviesRemoteDataSource = MoviesRemoteDataSourceImpl(apiServiceMovies)
        val moviesRepository = MoviesRepositoryImpl(moviesRemoteDataSource, filmLocalDataSource)
        val configuration = UseCase.Configuration(Dispatchers.IO)
        val getMoviesPageUseCase = GetMoviesPageUseCase(configuration, moviesRepository)
        val moviesConverter = MoviesConverter()
        val favoriteRemoteDataSource =
            FavoritesMoviesRemoteDataSourceImpl(apiServiceFavoritesMovies)
        val favoriteMoviesRepository = FavoriteMoviesRepositoryImpl(favoriteRemoteDataSource)
        val genreLocalDataSource = GenreLocalDataSourceImpl(dataBase.genreDao())
        val genreRepository = GenreRepositoryImpl(genreLocalDataSource)
        val addFilmToFavoriteUseCase =
            AddFavoriteMovieUseCase(configuration, favoriteMoviesRepository)
        val addFavoriteMovieConverter = AddFilmToFavoriteConverter()
        val addGenreUseCase = AddGenreUseCase(genreRepository, configuration)
        val addFavoriteGenreConverter = AddGenreToFavoriteConverter()
        val getGenreUseCase = GetGenreUseCase(genreRepository, configuration)
        val getGenresFromFavoriteConverter = GetGenresFromFavoriteConverter()
        val deleteGenreUseCase = DeleteGenreUseCase(genreRepository, configuration)
        val deleteGenreFromFavoriteConverter = DeleteGenreFromFavoriteConverter()
        val addFilmToDislikedConverter = AddFilmToDislikedConverter()
        val addFilmToDislikedUseCase = AddFilmToDislikedUseCase(moviesRepository, configuration)

        viewModel = ViewModelProvider(
            this, FeedViewModelFactory(
                getMoviesPageUseCase = getMoviesPageUseCase,
                moviesConverter = moviesConverter,
                addFilmToDislikedUseCase = addFilmToDislikedUseCase,
                filmConverter = addFilmToDislikedConverter,
                addFilmToFavoriteUseCase = addFilmToFavoriteUseCase,
                addFavoriteMovieConverter = addFavoriteMovieConverter,
                addGenreUseCase = addGenreUseCase,
                addFavoriteGenreConverter = addFavoriteGenreConverter,
                getGenreUseCase = getGenreUseCase,
                getGenresFromFavoriteConverter = getGenresFromFavoriteConverter,
                deleteGenreUseCase = deleteGenreUseCase,
                deleteGenreFromFavoriteConverter = deleteGenreFromFavoriteConverter
            )
        )[FeedViewModel::class]

        viewModel.load()
        viewModel.getAllFavoriteGenres()

        lifecycleScope.launch {
            viewModel.movies.collect {
                when (it) {
                    is UiState.Loading -> {}
                    is UiState.Success -> {
                        Log.e("feed_screen", it.data.toString())

                        moviesPagedListModel = it.data
                    }

                    is UiState.Error -> {
                        Log.e("feed_screen", it.errorMessage)
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.genres.collect {
                if (it != null) genreModels = it
            }
        }

        lifecycleScope.launch {
            viewModel.incomingStreams.collect {
                Log.d("feed_screen","+++---"+it)
                if (!it.any { el -> !el }) {
                    createCards()
                }
            }
        }

        lifecycleScope.launch {
            viewModel.allFavoriteGenres.collect {
                when (it) {
                    is UiState.Error -> {
                        Log.e("feed_screen", it.errorMessage)
                    }

                    UiState.Loading -> {
                        Log.d("feed_screen", "loading_all_genre")
                    }

                    is UiState.Success -> {
                        Log.d("feed_screen", it.data.toString())
                        genreModels = it.data
                    }
                }
            }
        }

    }

    private fun createCards() {
        adapter = CardStackAdapter(moviesPagedListModel)
        cardStackView = binding!!.cardStackView

        var cardPosition = 0

        manager = CardStackLayoutManager(requireContext(), object : CardStackListener {
            override fun onCardDragging(direction: Direction?, ratio: Float) {
                val card = manager.topPosition
                val holder =
                    cardStackView.findViewHolderForAdapterPosition(card) as? CardStackAdapter.ViewHolder
                val left = holder?.dislike
                val right = holder?.like

                when (direction) {
                    Direction.Right -> {
                        right?.alpha = ratio * 2
                        left?.alpha = 0f
                    }

                    Direction.Left -> {
                        left?.alpha = ratio * 2
                        right?.alpha = 0f
                    }

                    else -> {}

                }

            }


            override fun onCardSwiped(direction: Direction?) {
                when (direction) {
                    Direction.Right -> {
                        Toast.makeText(requireContext(), "Liked!", Toast.LENGTH_SHORT).show()
                        Log.d("feed_screen", cardPosition.toString())
                        onRightSwipe(adapter.getItemMovies(cardPosition))
                    }

                    Direction.Left -> {
                        Toast.makeText(requireContext(), "Disliked!", Toast.LENGTH_SHORT).show()
                        onLeftSwipe(adapter.getItemMovies(cardPosition))
                    }

                    else -> {}
                }

            }

            override fun onCardRewound() {

            }

            override fun onCardCanceled() {
                val card = manager.topPosition
                val holder =
                    cardStackView.findViewHolderForAdapterPosition(card) as? CardStackAdapter.ViewHolder
                val left = holder?.dislike
                val right = holder?.like
                right?.alpha = 0f
                left?.alpha = 0f
            }


            override fun onCardAppeared(view: View?, position: Int) {
                cardPosition = position

                val card = manager.topPosition
                val holder =
                    cardStackView.findViewHolderForAdapterPosition(card) as? CardStackAdapter.ViewHolder
                val left = holder?.dislike
                val right = holder?.like
                right?.alpha = 0f
                left?.alpha = 0f

                updateGenres(adapter.getItemMovies(position))

                view?.setOnClickListener {
                    val intent = Intent(requireContext(), MoviesDetailsActivity::class.java)
                    val movie = adapter.getItemMovies(position)
                    intent.putExtra("filmId", movie.id)
                    startActivity(intent)
                }
            }

            override fun onCardDisappeared(view: View?, position: Int) {
                Log.d("feed_screen","->  "+position+"___"+moviesPagedListModel.pageInfo?.pageSize)
                if (moviesPagedListModel.pageInfo?.pageSize == position + 1 &&
                    moviesPagedListModel.pageInfo?.currentPage != moviesPagedListModel.pageInfo?.pageCount
                )
                    viewModel.loadAgain()
                val layout = binding!!.genreLinearLayout
                layout.removeAllViews()
                binding!!.movieName.text =""
                binding!!.movieInfo.text=""
            }


        })

        manager.setStackFrom(StackFrom.None)
        manager.setMaxDegree(45.0f)
        manager.setDirections(Direction.HORIZONTAL)


        cardStackView.layoutManager = manager
        cardStackView.adapter = adapter
    }


    fun onLeftSwipe(moviesElementModel: MovieElementModel) {
        viewModel.addFilmToDisliked(moviesElementModel)
    }

    fun onRightSwipe(moviesElementModel: MovieElementModel) {
        viewModel.addFilmToFavorites(moviesElementModel)
    }

    private fun addToFavorite(genreModel: GenreModel) {
        viewModel.addGenre(genreModel)
    }

    private fun deleteFromFavorite(genreModel: GenreModel) {
        viewModel.deleteFavoriteGenre(genreModel)
    }


    @SuppressLint("SetTextI18n", "InflateParams")
    fun updateGenres(movie: MovieElementModel) {
        val layout = binding!!.genreLinearLayout
        layout.removeAllViews()

        binding!!.movieName.text = movie.moveName
        binding!!.movieInfo.text = movie.country + " • " + movie.year


        val maxWidth = layout.width

        var usedWidth = 0

        val spacingPx = (4 * layout.context.resources.displayMetrics.density).toInt()

        for (el in movie.genres.indices) {
            val textView =
                LayoutInflater.from(requireContext()).inflate(R.layout.genre_item, null) as TextView
            textView.text = movie.genres[el]?.genreName ?: ""

            textView.measure(
                View.MeasureSpec.UNSPECIFIED,
                View.MeasureSpec.makeMeasureSpec(layout.height, View.MeasureSpec.AT_MOST)
            )

            val itemWidth = textView.measuredWidth

            if (usedWidth + itemWidth + (spacingPx * el + 1) > maxWidth) break

            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.rightMargin = spacingPx
            textView.layoutParams = layoutParams

            var isFavorite = genreModels.any { it.id == movie.genres[el]!!.id }

            if (isFavorite) {
                textView.setBackgroundResource(R.drawable.gradient_accent)
            }

            textView.setOnClickListener {
                Log.d("feed_screen", movie.genres[el].toString())
                if (isFavorite) {
                    textView.setBackgroundResource(R.drawable.dark_faded_arounded)
                    deleteFromFavorite(movie.genres[el]!!)
                } else {
                    textView.setBackgroundResource(R.drawable.gradient_accent)
                    addToFavorite(movie.genres[el]!!)
                }
                isFavorite = !isFavorite
            }

            layout.addView(textView)
            usedWidth += itemWidth
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}