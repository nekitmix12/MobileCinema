package com.example.mobilecinema.presentation.movies

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.animation.Animation
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.mobilecinema.R
import com.example.mobilecinema.data.datasource.local.TokenStorageDataSourceImpl
import com.example.mobilecinema.data.datasource.remote.data_source.MoviesRemoteDataSourceImpl
import com.example.mobilecinema.data.model.movie.MovieElementModel
import com.example.mobilecinema.data.model.movie.MoviesPagedListModel
import com.example.mobilecinema.data.network.AuthInterceptor
import com.example.mobilecinema.data.network.NetworkModule
import com.example.mobilecinema.data.repository.MoviesRepositoryImpl
import com.example.mobilecinema.databinding.MoviesBinding
import com.example.mobilecinema.domain.UseCase
import com.example.mobilecinema.domain.use_case.auth_use_case.UiState
import com.example.mobilecinema.domain.use_case.movies_use_case.GetMoviesPageUseCase
import com.example.mobilecinema.presentation.feed.MoviesConverter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MoviesScreen : Fragment(R.layout.movies) {
    private var binding: MoviesBinding? = null
    private var viewModel: MoviesViewModel? = null
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: TopImagesAdapter
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
        viewModel = ViewModelProvider(
            this, MoviesViewModelFactory(getMoviesPageUseCase, moviesConverter)
        )[MoviesViewModel::class]

        viewPager = binding!!.topScrollView

        viewModel!!.setCommonId()

        viewModel!!.load()

        lifecycleScope.launch {
            viewModel!!.movies.collect {
                when (it) {
                    is UiState.Loading -> {}
                    is UiState.Error -> {}
                    is UiState.Success -> {
                        createViewPager(it.data)
                    }
                }
            }
        }


    }

    private fun createViewPager(movies: MoviesPagedListModel) {
        adapter = TopImagesAdapter(movies)
        viewPager.adapter = adapter
        var progressAnimator: ObjectAnimator? = null
        var progressBar: ProgressBar? = null
        var lastPosition: Int = 0
        viewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)


                progressAnimator?.cancel()
                if(lastPosition==position+1)
                progressBar?.progress = 0
                else
                    progressBar?.progress = 100
                lastPosition = position

                progressBar = getProgressBar(position)
                progressAnimator = getAnimator(progressBar)
                progressAnimator?.addListener(object : AnimatorListenerAdapter(){
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        Log.e("movies_screen",position.toString())
                        //viewPager.setCurrentItem(position+1,true)
                    }
                })
                progressAnimator?.start()
                changeFilmInfo(movies.movies!![position])
            }
        })
    }

    fun changeFilmInfo(moviesElement:MovieElementModel){
        val relativeLayout = binding!!.genreMoviesRelativeLayout
        relativeLayout.removeAllViews()
        var text:TextView?
        var uniqueId = 0
        binding!!.moviesName.text = moviesElement.moveName
        for(i in 0..2) {
            if(moviesElement.genres.size==i-1)
                return
            text = TextView(requireContext())
            text.setPadding(15.dpToPx(),5.dpToPx(),15.dpToPx(),5.dpToPx())
            text.setBackgroundResource(R.drawable.dark_faded_arounded)
            text.setTextColor(Color.WHITE)


            val param = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(5.dpToPx(), 5.dpToPx(), 5.dpToPx(), 5.dpToPx())
            }

            text.id = View.generateViewId()
            when(i){
                0->{
                    uniqueId = text.id
                }
                1->{
                    param.addRule(RelativeLayout.END_OF,uniqueId)

                }
                2->{
                    param.addRule(RelativeLayout.BELOW,uniqueId)

                }
            }
            text.layoutParams = param
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
        return progressBar
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}