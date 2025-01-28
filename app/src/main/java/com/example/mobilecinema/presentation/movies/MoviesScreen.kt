package com.example.mobilecinema.presentation.movies

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobilecinema.R
import com.example.mobilecinema.data.model.movie.GenreModel
import com.example.mobilecinema.databinding.MoviesBinding
import com.example.mobilecinema.presentation.adapter.AdapterWithDelegates
import com.example.mobilecinema.presentation.adapter.EndScrollListener
import com.example.mobilecinema.presentation.adapter.decorators.PaddingItemDecoration
import com.example.mobilecinema.presentation.adapter.delegates.AllFilmDelegate
import com.example.mobilecinema.presentation.adapter.delegates.ButtonDelegate
import com.example.mobilecinema.presentation.adapter.delegates.CarouselDelegate
import com.example.mobilecinema.presentation.adapter.delegates.FavoriteDelegate
import com.example.mobilecinema.presentation.adapter.delegates.HeadingDelegate
import com.example.mobilecinema.presentation.adapter.delegates.HorizontalItemDelegates
import com.example.mobilecinema.presentation.adapter.delegates.HorizontalWithCarouselDelegate
import com.example.mobilecinema.presentation.adapter.delegates.LabelDelegate
import com.example.mobilecinema.presentation.favorite.navigateToActivity
import com.example.mobilecinema.presentation.movies_details.MoviesDetailsActivity
import kotlinx.coroutines.launch

class MoviesScreen : Fragment(R.layout.movies) {
    private lateinit var binding: MoviesBinding
    private lateinit var viewModel: MoviesViewModel

    private var adapterAllElement = AdapterWithDelegates(
        listOf(
            HorizontalItemDelegates(
                listOf(
                    FavoriteDelegate(), CarouselDelegate(
                        genreOnClick = ::genreOnClick, buttonOnClick = ::buttonOnClick
                    ),
                    AllFilmDelegate(::buttonOnClick)
                ), 70
            ), HorizontalWithCarouselDelegate(
                listOf(
                    FavoriteDelegate(), CarouselDelegate(
                        genreOnClick = ::genreOnClick, buttonOnClick = ::buttonOnClick
                    )
                ), listOf(), 70
            ),
            ButtonDelegate(::randomButtonOnClick),
            HeadingDelegate(::allFavoriteOnClick),
            LabelDelegate()

        )
    )

    /*    private lateinit var allFilmsAdapter: AllMoviesAdapter
        private lateinit var favoritesMoviesAdapter: FavoriteMoviesAdapter
        private lateinit var moviesPagedListModel: MoviesPagedListModel*/


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = MoviesBinding.bind(view)

        viewModel = ViewModelProvider(
            this, MoviesViewModelFactory()
        )[MoviesViewModel::class]

        lifecycleScope.launch {

            launch {
                viewModel.feed.collect {
                    if (it.isNotEmpty()) {
                        binding.loadingScreen.visibility = View.GONE
                        adapterAllElement.submitList(it)
                    } else binding.loadingScreen.visibility = View.VISIBLE
                }
            }
        }

        with(binding.recyclerView) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = adapterAllElement
            addItemDecoration(
                PaddingItemDecoration(
                    32,
                    16,
                    24,
                    24,
                    R.layout.movies_favorite_heading,
                    R.layout.label_all_films
                )
            )
            addItemDecoration(
                PaddingItemDecoration(
                    32,
                    0,
                    24,
                    24,
                    R.layout.random_button
                )
            )
            addItemDecoration(
                PaddingItemDecoration(
                    4,
                    4,
                    4,
                    4,
                    R.layout.movies_element_all_films
                )
            )
            addItemDecoration(
                PaddingItemDecoration(
                    0,
                    0,
                    20,
                    20,
                    R.layout.horizontal_recycle_view
                )
            )
            addOnScrollListener(EndScrollListener(::onScrollEnded))
        }

    }


    private fun randomButtonOnClick() {

    }

    private fun genreOnClick(isFavorite: Boolean, genre: GenreModel) {
        if (isFavorite) viewModel.deleteFavoriteGenre(genre)
        else viewModel.addGenre(genre)
    }

    private fun buttonOnClick(movieId: String) {
        requireContext().navigateToActivity(MoviesDetailsActivity::class.java, movieId)

    }

    private fun allFavoriteOnClick() {
        Log.d("genre", "allFavoriteOnClick")
    }

    private fun onScrollEnded() {
        lifecycleScope.launch {
            viewModel.loadMovies()
        }
    }
    /*    private fun*//*
        private fun createFavoritesElementRecycleView(movies: MoviesListModel) {
            val recyclerView = binding.favoriteMoviesRecyclerView
            recyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            favoritesMoviesAdapter = FavoriteMoviesAdapter(movies.movies)
            recyclerView.adapter = favoritesMoviesAdapter
            var positionLastUpdated = 0
            var isScale = false
            recyclerView.addOnScrollListener(object : RecyclerView.)
        }

        private fun createAllMoviesRecycleView(movies: MoviesPagedListModel, ratings: List<Float>) {
            val recycleView = binding.allFilmsRecycle
            recycleView.layoutManager = GridLayoutManager(requireContext(), 3)
            allFilmsAdapter = AllMoviesAdapter(movies.movies, ratings)
            recycleView.adapter = allFilmsAdapter

        }*//*    private fun getCarouselUiEvent(): EventHandler<UiEvent> = object : EventHandler<UiEvent> {
            override fun handle(event: UiEvent) {
                when (event) {
                    is UiEvent.GenreClicked -> {
                        Toast.makeText(
                            requireContext(),
                            "Image clicked: ${event.genre.genreName}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is UiEvent.CarouselButtonClicked -> {
                        Toast.makeText(
                            requireContext(), "Name clicked: ${event.moviesId}", Toast.LENGTH_SHORT
                        ).show()
                    }
                    else->{}
                }
            }

        }*/

    /*    private fun createViewPager(movies: List<CarouselModel>) {
            adapter = AdapterWithDelegates(eventHandler = getCarouselUiEvent())
            viewPager.adapter = adapter
            adapter.setItem(movies)*/
}/*var progressAnimator: ObjectAnimator? = null
    var progressBar: ProgressBar? = null
    var lastPosition = 0
    viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)

            val currentPosition = if (position == 5) 0
            else position
            progressAnimator?.cancel()
            if (lastPosition == currentPosition + 1) progressBar?.progress = 0
            else {
                progressBar?.progress = 100
            }
            lastPosition = currentPosition

            if (viewPager.currentItem == 5) {
                viewPager.setCurrentItem(0, true)
                for (progressBarPos in 0..4) {
                    getProgressBar(progressBarPos)?.progress = 0

                }
            }

            progressBar = getProgressBar(currentPosition)
            progressAnimator = getAnimator(progressBar)
            progressAnimator?.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    if (viewPager.currentItem == currentPosition) viewPager.setCurrentItem(
                        currentPosition + 1, true
                    )

                }
            })
            progressAnimator?.start()
            currentMovies.movies?.let { changeFilmInfo(it[currentPosition]) }
        }
    })*/


/*    fun changeFilmInfo(moviesElement: MovieElementModel) {
        val relativeLayout = binding.genreMoviesRelativeLayout
        relativeLayout.removeAllViews()
        var text: TextView?
        var uniqueId = 0
        binding.moviesName.text = moviesElement.moveName
        for (i in 0..2) {
            if (moviesElement.genres.size == i - 1) return
            text = TextView(requireContext())
            text.setPadding(15.dpToPx(), 5.dpToPx(), 15.dpToPx(), 5.dpToPx())
            text.setBackgroundResource(R.drawable.dark_faded_arounded)
            text.setTextColor(Color.WHITE)


            val param = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT
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
            if (moviesElement.genres.size == i + 1) break
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
    }*//*

    fun getProgressBar(number: Int): ProgressBar? {
        var progressBar: ProgressBar? = null
        when (number) {
            0 -> progressBar = binding.firstProgressBar


            1 -> progressBar = binding.secondProgressBar


            2 -> progressBar = binding.thirdProgressBar


            3 -> progressBar = binding.fourthProgressBar


            4 -> progressBar = binding.fifthProgressBar


            else -> return null

        }
        return progressBar
    }


}*/
