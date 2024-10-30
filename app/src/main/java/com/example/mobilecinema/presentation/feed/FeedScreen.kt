package com.example.mobilecinema.presentation.feed

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.mobilecinema.R
import com.example.mobilecinema.data.datasource.local.TokenStorageDataSourceImpl
import com.example.mobilecinema.data.datasource.remote.data_source.MoviesRemoteDataSourceImpl
import com.example.mobilecinema.data.model.movie.MoviesPagedListModel
import com.example.mobilecinema.data.network.AuthInterceptor
import com.example.mobilecinema.data.network.NetworkModule
import com.example.mobilecinema.data.repository.MoviesRepositoryImpl
import com.example.mobilecinema.databinding.FeedScreenBinding
import com.example.mobilecinema.domain.UseCase
import com.example.mobilecinema.domain.converters.MoviesConverter
import com.example.mobilecinema.domain.use_case.auth_use_case.UiState
import com.example.mobilecinema.domain.use_case.movies_use_case.GetMoviesPageUseCase
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.CardStackView
import com.yuyakaido.android.cardstackview.Direction
import com.yuyakaido.android.cardstackview.StackFrom
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FeedScreen : Fragment(R.layout.feed_screen) {
    private lateinit var cardStackView: CardStackView
    private lateinit var manager: CardStackLayoutManager
    private lateinit var adapter: CardStackAdapter
    private lateinit var viewModel: FeedViewModel
    private var binding: FeedScreenBinding? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FeedScreenBinding.bind(view)

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
            this, FeedViewModelFactory(getMoviesPageUseCase, moviesConverter)
        )[FeedViewModel::class]

        viewModel.load()

        lifecycleScope.launch {
            viewModel.movies.collect {
                when (it) {
                    is UiState.Loading -> {}
                    is UiState.Success -> {
                        Log.e("feed_screen", it.data.toString())
                        Toast.makeText(requireContext(), it.data.toString(), Toast.LENGTH_LONG)
                            .show()
                        createCards(it.data)
                    }

                    is UiState.Error -> {
                        Log.e("feed_screen", it.errorMessage)
                        Toast.makeText(requireContext(), it.errorMessage, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

    }

    private fun createCards(moviesPagedListModel: MoviesPagedListModel) {
        var filmCounter = 0

        val layout = binding!!.genreLinearLayout
        val move = moviesPagedListModel.movies!![filmCounter]
        layout.removeAllViews()
        for(i in 0..2){

            if(i == move.genres.size-1)
                return
            val text = TextView(requireContext())
            text.text = move.genres[i]!!.genreName
            text.setBackgroundResource(R.drawable.sing_in_input_text)
            text.textSize = 16f
            text.setTextColor(Color.RED)
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            layoutParams.setMargins(10,0,0,10)
            text.setLayoutParams(layoutParams)
            layout.addView(text)
        }
        for (i in 0 until layout.childCount) {
            val child = layout.getChildAt(i)
            Log.d("ChildView", "Child $i: $child")
        }
        layout.invalidate()
        layout.requestLayout()

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

                    }

                    Direction.Left -> {
                        Toast.makeText(requireContext(), "Disliked!", Toast.LENGTH_SHORT).show()

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
                val card = manager.topPosition
                val holder =
                    cardStackView.findViewHolderForAdapterPosition(card) as? CardStackAdapter.ViewHolder
                val left = holder?.dislike
                val right = holder?.like
                right?.alpha = 0f
                left?.alpha = 0f



                if (filmCounter < moviesPagedListModel.movies.size) {

                    binding!!.movieName.text = move.moveName
                    binding!!.movieInfo.text =
                        move.country + " • " + move.year
                    filmCounter++
                }


                layout.removeAllViews()
                for(i in 0..2){

                    if(i == move.genres.size-1)
                        return

                    val text = TextView(requireContext())
                    text.text = move.genres[i]!!.genreName

                    text.setBackgroundResource(R.drawable.sing_in_input_text)
                    text.textSize = 16f

                    val layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    layoutParams.setMargins(10,0,0,10)
                    text.setLayoutParams(layoutParams)
                    layout.addView(text)
                }
            }

            override fun onCardDisappeared(view: View?, position: Int) {
            }


        })

        manager.setStackFrom(StackFrom.None)
        manager.setMaxDegree(45.0f)
        manager.setDirections(Direction.HORIZONTAL)
        cardStackView = binding!!.cardStackView
        adapter = CardStackAdapter(moviesPagedListModel)
        cardStackView.layoutManager = manager
        cardStackView.adapter = adapter
    }


    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}