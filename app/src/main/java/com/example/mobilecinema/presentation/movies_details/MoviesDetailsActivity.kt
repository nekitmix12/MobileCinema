package com.example.mobilecinema.presentation.movies_details

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.example.mobilecinema.R
import com.example.mobilecinema.data.datasource.local.TokenStorageDataSourceImpl
import com.example.mobilecinema.data.datasource.remote.data_source.KinopoiskRemoteDataSourceImpl
import com.example.mobilecinema.data.datasource.remote.data_source.MoviesRemoteDataSourceImpl
import com.example.mobilecinema.data.datasource.remote.data_source.ReviewRemoteDataSourceImpl
import com.example.mobilecinema.data.network.AuthInterceptor
import com.example.mobilecinema.data.network.NetworkModule
import com.example.mobilecinema.data.repository.KinopoiskRepositoryImpl
import com.example.mobilecinema.data.repository.MoviesRepositoryImpl
import com.example.mobilecinema.data.repository.ReviewRepositoryImpl
import com.example.mobilecinema.domain.MoviesFilmRatingImpl
import com.example.mobilecinema.domain.UseCase
import com.example.mobilecinema.domain.converters.FilmConverter
import com.example.mobilecinema.domain.converters.MovieDetailsConverter
import com.example.mobilecinema.domain.converters.MoviesRatingConverter
import com.example.mobilecinema.domain.converters.SearchFilmConverter
import com.example.mobilecinema.domain.use_case.kinopoisk_use_case.GetFilmUseCase
import com.example.mobilecinema.domain.use_case.kinopoisk_use_case.SearchFilmUseCase
import com.example.mobilecinema.domain.use_case.movies_use_case.GetMoviesDetailsUseCase
import com.example.mobilecinema.domain.use_case.movies_use_case.MoviesRatingUseCase
import com.example.mobilecinema.domain.use_case.review_use_case.AddReviewUseCase
import com.example.mobilecinema.domain.use_case.review_use_case.DeleteReviewUseCase
import com.example.mobilecinema.domain.use_case.review_use_case.EditReviewUseCase
import kotlinx.coroutines.Dispatchers

class MoviesDetailsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = this.getSharedPreferences(
            this.getString(R.string.preference_is_logged_in), Context.MODE_PRIVATE
        )
        val tokenStorage = TokenStorageDataSourceImpl(sharedPreferences)
        val networkModule = NetworkModule()
        val interceptor = AuthInterceptor(tokenStorage)

        val configuration = UseCase.Configuration(Dispatchers.IO)

        val apiServiceKinopoisk = networkModule.providerKinopoiskService(
            networkModule.provideRetrofit(
                networkModule.provideOkHttpClient(interceptor)
            )
        )
        val apiServiceReview = networkModule.provideReviewService(
            networkModule.provideRetrofit(
                networkModule.provideOkHttpClient(interceptor)
            )
        )
        val apiServiceMovies = networkModule.provideMovieService(
            networkModule.provideRetrofit(
                networkModule.provideOkHttpClient(interceptor)
            )
        )

        val reviewRemoteDataSource = ReviewRemoteDataSourceImpl(apiServiceReview)
        val kinopoiskRemoteDataSource = KinopoiskRemoteDataSourceImpl(apiServiceKinopoisk)
        val moviesRemoteDataSource = MoviesRemoteDataSourceImpl(apiServiceMovies)

        val reviewRepository = ReviewRepositoryImpl(reviewRemoteDataSource)
        val kinopoiskRepository = KinopoiskRepositoryImpl(kinopoiskRemoteDataSource)
        val moviesRepository = MoviesRepositoryImpl(moviesRemoteDataSource)

        val addReviewUseCase = AddReviewUseCase(reviewRepository, configuration)
        val editReviewUseCase = EditReviewUseCase(reviewRepository, configuration)
        val deleteReviewUseCase = DeleteReviewUseCase(reviewRepository, configuration)

        val getFilmUseCase = GetFilmUseCase(kinopoiskRepository, configuration)
        val searchFilmUseCase = SearchFilmUseCase(kinopoiskRepository, configuration)

        val getMoviesDetailsUseCase = GetMoviesDetailsUseCase(moviesRepository, configuration)

        val movieDetailsConverter = MovieDetailsConverter()
        val searchFilmConverter = SearchFilmConverter()
        val filmConverter = FilmConverter()
        val moviesFilmRating = MoviesFilmRatingImpl()
        val moviesRatingUseCase = MoviesRatingUseCase(moviesFilmRating, configuration)
        val moviesRatingConverter = MoviesRatingConverter()

        val viewModel = ViewModelProvider(
            this,
            MoviesDetailViewModelFactory(
                addReviewUseCase,
                editReviewUseCase,
                deleteReviewUseCase,
                getFilmUseCase,
                searchFilmUseCase,
                getMoviesDetailsUseCase,
                movieDetailsConverter,
                searchFilmConverter,
                filmConverter,
                moviesRatingConverter,
                moviesRatingUseCase,

            )
        )[MoviesDetailViewModel::class]
        intent.getStringExtra("FILM_NAME")?.let { viewModel.loadDetails(it) }
        setContent {
            MoviesDetailsScreen(viewModel)
        }
    }
}

