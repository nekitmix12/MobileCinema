package com.example.mobilecinema.presentation.favorite

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.mobilecinema.R
import com.example.mobilecinema.data.datasource.local.AppDataBase
import com.example.mobilecinema.data.datasource.local.TokenStorageDataSourceImpl
import com.example.mobilecinema.data.datasource.local.data_source.FilmLocalDataSourceImpl
import com.example.mobilecinema.data.datasource.local.data_source.GenreLocalDataSourceImpl
import com.example.mobilecinema.data.datasource.remote.data_source.FavoritesMoviesRemoteDataSourceImpl
import com.example.mobilecinema.data.datasource.remote.data_source.MoviesRemoteDataSourceImpl
import com.example.mobilecinema.data.network.AuthInterceptor
import com.example.mobilecinema.data.network.NetworkModule
import com.example.mobilecinema.data.repository.FavoriteMoviesRepositoryImpl
import com.example.mobilecinema.data.repository.GenreRepositoryImpl
import com.example.mobilecinema.data.repository.MoviesRepositoryImpl
import com.example.mobilecinema.domain.MoviesFilmRatingImpl
import com.example.mobilecinema.domain.UseCase
import com.example.mobilecinema.domain.converters.FilmToDislikedConverter
import com.example.mobilecinema.domain.converters.MoviesRatingConverter
import com.example.mobilecinema.domain.converters.film.AddFilmToFavoriteConverter
import com.example.mobilecinema.domain.converters.film.FavoriteMoviesConverter
import com.example.mobilecinema.domain.converters.film.MoviesConverter
import com.example.mobilecinema.domain.converters.genre.AddGenreToFavoriteConverter
import com.example.mobilecinema.domain.converters.genre.DeleteGenreFromFavoriteConverter
import com.example.mobilecinema.domain.converters.genre.GetGenresFromFavoriteConverter
import com.example.mobilecinema.domain.use_case.favorite_movies_use_case.AddFavoriteMovieUseCase
import com.example.mobilecinema.domain.use_case.favorite_movies_use_case.GetFavoriteMoviesUseCase
import com.example.mobilecinema.domain.use_case.genre.AddGenreUseCase
import com.example.mobilecinema.domain.use_case.genre.DeleteGenreUseCase
import com.example.mobilecinema.domain.use_case.genre.GetGenreUseCase
import com.example.mobilecinema.domain.use_case.movies_use_case.AddFilmToDislikedUseCase
import com.example.mobilecinema.domain.use_case.movies_use_case.GetMoviesPageUseCase
import com.example.mobilecinema.domain.use_case.movies_use_case.MoviesRatingUseCase
import com.example.mobilecinema.presentation.feed.FeedViewModel
import com.example.mobilecinema.presentation.feed.FeedViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoritesScreen:Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
        val configuration = UseCase.Configuration(Dispatchers.IO)

        val favoriteRemoteDataSource =
            FavoritesMoviesRemoteDataSourceImpl(apiServiceFavoritesMovies)
        val favoriteMoviesRepository = FavoriteMoviesRepositoryImpl(favoriteRemoteDataSource)
        val genreLocalDataSource = GenreLocalDataSourceImpl(dataBase.genreDao())
        val genreRepository = GenreRepositoryImpl(genreLocalDataSource)
        val getFavoriteMoviesUseCase = GetFavoriteMoviesUseCase(configuration,favoriteMoviesRepository)
        val favoriteMoviesConverter = FavoriteMoviesConverter()
        val moviesRatingConverter = MoviesRatingConverter()
        val moviesFilmRating = MoviesFilmRatingImpl()
        val moviesRatingUseCase = MoviesRatingUseCase(moviesFilmRating, configuration)
        val getGenreUseCase = GetGenreUseCase(genreRepository, configuration)
        val getGenresFromFavoriteConverter = GetGenresFromFavoriteConverter()
        val deleteGenreUseCase = DeleteGenreUseCase(genreRepository, configuration)
        val deleteGenreFromFavoriteConverter = DeleteGenreFromFavoriteConverter()
        val viewModel = ViewModelProvider(
            this, FavoriteMoviesViewModelFactory(
                getFavoriteMoviesUseCase = getFavoriteMoviesUseCase,
                favoriteMoviesConverter = favoriteMoviesConverter,
                moviesRatingUseCase = moviesRatingUseCase,
                moviesRatingConverter = moviesRatingConverter,
                deleteGenreUseCase = deleteGenreUseCase,
                deleteGenreFromFavoriteConverter = deleteGenreFromFavoriteConverter,
                getGenresFromFavoriteConverter = getGenresFromFavoriteConverter,
                getGenreUseCase = getGenreUseCase
            )
        )[FavoriteViewModel::class]

        lifecycleScope.launch {
            viewModel.getFavoritesFilms()
            viewModel.getAllFavoriteGenres()
            viewModel.loadRatings()
        }
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                FavoriteScreen(
                    viewModel
                )
            }
        }
    }
}