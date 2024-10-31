package com.example.mobilecinema.presentation.movies

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.mobilecinema.R
import com.example.mobilecinema.data.datasource.local.TokenStorageDataSourceImpl
import com.example.mobilecinema.data.datasource.remote.data_source.FavoritesMoviesRemoteDataSourceImpl
import com.example.mobilecinema.data.datasource.remote.data_source.MoviesRemoteDataSourceImpl
import com.example.mobilecinema.data.model.favorite_movies.MoviesListModel
import com.example.mobilecinema.data.model.movie.MovieElementModel
import com.example.mobilecinema.data.model.movie.MoviesPagedListModel
import com.example.mobilecinema.data.network.AuthInterceptor
import com.example.mobilecinema.data.network.NetworkModule
import com.example.mobilecinema.data.repository.FavoriteMoviesRepositoryImpl
import com.example.mobilecinema.data.repository.MoviesRepositoryImpl
import com.example.mobilecinema.databinding.MoviesBinding
import com.example.mobilecinema.domain.MoviesFilmRatingImpl
import com.example.mobilecinema.domain.UseCase
import com.example.mobilecinema.domain.converters.FavoriteMoviesConverter
import com.example.mobilecinema.domain.converters.MoviesConverter
import com.example.mobilecinema.domain.converters.MoviesRatingConverter
import com.example.mobilecinema.domain.use_case.UiState
import com.example.mobilecinema.domain.use_case.favorite_movies_use_case.GetFavoriteMoviesUseCase
import com.example.mobilecinema.domain.use_case.movies_use_case.MoviesRatingUseCase
import com.example.mobilecinema.domain.use_case.movies_use_case.GetMoviesPageUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MoviesScreen : Fragment(R.layout.movies) {
    private var binding: MoviesBinding? = null
    private var viewModel: MoviesViewModel? = null
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: TopImagesAdapter
    private lateinit var allFilmsAdapter: AllMoviesAdapter
    private lateinit var favoritesMoviesAdapter: FavoriteMoviesAdapter
    private lateinit var moviesPagedListModel: MoviesPagedListModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = MoviesBinding.bind(view)

        val sharedPreferences = requireContext().getSharedPreferences(
            requireContext().getString(R.string.preference_is_logged_in), Context.MODE_PRIVATE
        )
        sharedPreferences.getString("authToken", "")?.let { Log.d("feed_screen", it) }
        val tokenStorage = TokenStorageDataSourceImpl(sharedPreferences)
        val networkModule = NetworkModule()
        val interceptor = AuthInterceptor(tokenStorage)
        val apiServiceMovies = networkModule.provideMovieService(
            networkModule.provideRetrofit(
                networkModule.provideOkHttpClient(interceptor)
            )
        )
        val moviesRemoteDataSource = MoviesRemoteDataSourceImpl(apiServiceMovies)
        val moviesRepository = MoviesRepositoryImpl(moviesRemoteDataSource)
        val configuration = UseCase.Configuration(Dispatchers.IO)
        val getMoviesPageUseCase = GetMoviesPageUseCase(configuration, moviesRepository)
        val moviesConverter = MoviesConverter()
        val apiServiceFavoriteMovies = networkModule.provideFavoriteMoviesService(
            networkModule.provideRetrofit(
                networkModule.provideOkHttpClient(interceptor)
            )
        )
        val favoriteMoviesRemoteDataSourceImpl =
            FavoritesMoviesRemoteDataSourceImpl(apiServiceFavoriteMovies)
        val favoriteMoviesRepository =
            FavoriteMoviesRepositoryImpl(favoriteMoviesRemoteDataSourceImpl)
        val getFavoriteMoviesUseCase =
            GetFavoriteMoviesUseCase(configuration, favoriteMoviesRepository)
        val favoriteMoviesConverter = FavoriteMoviesConverter()
        val moviesRatingConverter = MoviesRatingConverter()
        val moviesFilmRating = MoviesFilmRatingImpl()
        val moviesRatingUseCase = MoviesRatingUseCase(moviesFilmRating, configuration)
        viewModel = ViewModelProvider(
            this,
            MoviesViewModelFactory(
                getMoviesPageUseCase,
                moviesConverter,
                getFavoriteMoviesUseCase,
                favoriteMoviesConverter,
                moviesRatingUseCase,
                moviesRatingConverter
            )
        )[MoviesViewModel::class]

        viewPager = binding!!.topScrollView

        viewModel!!.setCommonId()

        lifecycleScope.launch {
            viewModel!!.loadMovies()
            viewModel!!.loadRatings()
        }

        lifecycleScope.launch {
            viewModel!!.loadFavoritesMovies()
        }

        lifecycleScope.launch {
            viewModel!!.moviesFavorite.collect {
                when (it) {
                    is UiState.Loading -> {}
                    is UiState.Error -> {}
                    is UiState.Success -> {
                        createFavoritesElementRecycleView(it.data)
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel!!.movies.collect {
                when (it) {
                    is UiState.Loading -> {}
                    is UiState.Error -> {}
                    is UiState.Success -> {
                        createViewPager(MoviesListModel(it.data.movies))
                        moviesPagedListModel = it.data
                        createAllMoviesRecycleView(it.data, listOf(0.2f))
                    }
                }
            }


        }
        lifecycleScope.launch {
            viewModel!!.moviesRating.collect {
                when (it) {
                    is UiState.Loading -> {}
                    is UiState.Error -> {}
                    is UiState.Success -> {
                        createAllMoviesRecycleView(moviesPagedListModel, it.data)
                    }
                }
            }
        }


    }

    private fun createFavoritesElementRecycleView(movies: MoviesListModel) {
        val recyclerView = binding!!.favoriteMoviesRecyclerView
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        favoritesMoviesAdapter = FavoriteMoviesAdapter(movies.movies)
        recyclerView.adapter = favoritesMoviesAdapter
        var positionLastUpdated = 0
        var isScale = false
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as? LinearLayoutManager

                val firstVisiblePosition = layoutManager!!.findFirstVisibleItemPosition()
                val lastVisiblePosition = layoutManager.findLastVisibleItemPosition()

                for (position in firstVisiblePosition..lastVisiblePosition) {
                    val viewHolder = recyclerView.findViewHolderForAdapterPosition(position)

                    val itemView = viewHolder?.itemView

                    if (itemView?.left!! >= 0 && itemView.right <= recyclerView.width) {
                        if (position != positionLastUpdated) {
                            val item =
                                recyclerView.findViewHolderForAdapterPosition(positionLastUpdated)
                            item?.itemView?.scaleX = 1.0f
                            item?.itemView?.scaleY = 1.0f
                            positionLastUpdated = position
                            isScale = false
                        } else if (!isScale) {
                            val item = recyclerView.findViewHolderForAdapterPosition(position)
                            item?.itemView?.scaleX = 1.2f
                            item?.itemView?.scaleY = 1.2f
                            isScale = true
                        }
                        break
                    }


                }
            }
        })
    }

    private fun createAllMoviesRecycleView(movies: MoviesPagedListModel, ratings: List<Float>) {
        val recycleView = binding!!.allFilmsRecycle
        recycleView.layoutManager = GridLayoutManager(requireContext(), 3)
        allFilmsAdapter = AllMoviesAdapter(movies.movies, ratings)
        recycleView.adapter = allFilmsAdapter

    }

    private fun createViewPager(movies: MoviesListModel) {
        val currentMovies = MoviesListModel(movies = movies.movies!!.dropLast(1) + movies.movies[0])
        adapter = TopImagesAdapter(currentMovies)
        viewPager.adapter = adapter
        var progressAnimator: ObjectAnimator? = null
        var progressBar: ProgressBar? = null
        var lastPosition = 0
        viewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                val currentPosition = if(position==5)
                    0
                else
                    position
                progressAnimator?.cancel()
                if (lastPosition == currentPosition + 1)
                    progressBar?.progress = 0
                else {
                    progressBar?.progress = 100
                }
                lastPosition = currentPosition

                if(viewPager.currentItem == 5) {
                    viewPager.setCurrentItem(0, true)
                    for(progressBarPos in 0..4){
                        getProgressBar(progressBarPos)?.progress = 0

                    }
                }

                progressBar = getProgressBar(currentPosition)
                progressAnimator = getAnimator(progressBar)
                Log.e("movies_screen", "$position, $currentPosition")
                progressAnimator?.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        Log.e("movies_screen", currentPosition.toString()+"0")
                        if (viewPager.currentItem == currentPosition)
                            viewPager.setCurrentItem(currentPosition + 1, true)

                    }
                })
                Log.e("movies_screen", currentPosition.toString()+"1")
                progressAnimator?.start()
                changeFilmInfo(currentMovies.movies!![currentPosition])
            }
        })
    }

    fun changeFilmInfo(moviesElement: MovieElementModel) {
        val relativeLayout = binding!!.genreMoviesRelativeLayout
        relativeLayout.removeAllViews()
        var text: TextView?
        var uniqueId = 0
        binding!!.moviesName.text = moviesElement.moveName
        for (i in 0..2) {
            if (moviesElement.genres.size == i - 1)
                return
            text = TextView(requireContext())
            text.setPadding(15.dpToPx(), 5.dpToPx(), 15.dpToPx(), 5.dpToPx())
            text.setBackgroundResource(R.drawable.dark_faded_arounded)
            text.setTextColor(Color.WHITE)


            val param = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(5.dpToPx(), 5.dpToPx(), 5.dpToPx(), 5.dpToPx())
            }

            text.id = View.generateViewId()
            when (i) {
                0 -> {
                    uniqueId = text.id
                }

                1 -> {
                    param.addRule(RelativeLayout.END_OF, uniqueId)

                }

                2 -> {
                    param.addRule(RelativeLayout.BELOW, uniqueId)

                }
            }
            text.layoutParams = param
            Log.e("moviesScreen",moviesElement.genres.toString())
            if(moviesElement.genres.size==i+1)
                break
            text.text = moviesElement.genres[i]?.genreName

            relativeLayout.addView(text)
        }
    }

    private fun Int.dpToPx(): Int = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), resources.displayMetrics
    ).toInt()

    fun getAnimator(progressBar: ProgressBar?): ObjectAnimator? {
        progressBar?.let {
            val progressAnimator = ObjectAnimator.ofInt(progressBar, "progress", 0, 100)
            progressAnimator.duration = 5000
            progressAnimator.interpolator =
                android.view.animation.AccelerateDecelerateInterpolator()
            return progressAnimator
        }
        return null
    }

    fun getProgressBar(number: Int): ProgressBar? {
        var progressBar: ProgressBar? = null
        if (binding != null) {
            when (number) {
                0 -> {
                    progressBar = binding!!.firstProgressBar

                }

                1 -> {
                    progressBar = binding!!.secondProgressBar

                }

                2 -> {
                    progressBar = binding!!.thirdProgressBar

                }

                3 -> {
                    progressBar = binding!!.fourthProgressBar

                }

                4 -> {
                    progressBar = binding!!.fifthProgressBar

                }

                else -> return null

            }
        }
        return progressBar
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}