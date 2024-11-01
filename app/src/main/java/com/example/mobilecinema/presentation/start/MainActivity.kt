package com.example.mobilecinema.presentation.start

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.mobilecinema.R
import com.example.mobilecinema.data.datasource.local.TokenStorageDataSourceImpl
import com.example.mobilecinema.data.datasource.remote.data_source.AuthRemoteDataSourceImpl
import com.example.mobilecinema.data.datasource.remote.data_source.UserRemoteDataSourceImpl
import com.example.mobilecinema.data.network.AuthInterceptor
import com.example.mobilecinema.data.network.NetworkModule
import com.example.mobilecinema.data.repository.AuthRepositoryImpl
import com.example.mobilecinema.data.repository.UserRepositoryImpl
import com.example.mobilecinema.domain.UseCase
import com.example.mobilecinema.domain.converters.ProfileConverter
import com.example.mobilecinema.domain.use_case.UiState
import com.example.mobilecinema.domain.use_case.auth_use_case.LogOutUseCase
import com.example.mobilecinema.domain.use_case.user_use_case.GetProfileUseCase
import com.example.mobilecinema.domain.use_case.user_use_case.PutProfileUseCase
import com.example.mobilecinema.presentation.CinemaActivity
import com.example.mobilecinema.presentation.WelcomeActivity
import com.example.mobilecinema.presentation.profile.ProfileViewModel
import com.example.mobilecinema.presentation.profile.ProfileViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val sharedPref: SharedPreferences by lazy {
        baseContext.getSharedPreferences(
            this.getString(R.string.preference_is_logged_in),
            Context.MODE_PRIVATE
        )
    }
    private var viewModel: MainViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val sharedPreferences = this.getSharedPreferences(
            this.getString(R.string.preference_is_logged_in), Context.MODE_PRIVATE
        )
        sharedPreferences.getString("authToken", "")?.let { Log.d("feed_screen", it) }
        val tokenStorage = TokenStorageDataSourceImpl(sharedPreferences)
        val networkModule = NetworkModule()
        val interceptor = AuthInterceptor(tokenStorage)
        val apiServiceUser = networkModule.provideUserService(
            networkModule.provideRetrofit(
                networkModule.provideOkHttpClient(interceptor)
            )
        )
        val userRemoteDataSource = UserRemoteDataSourceImpl(apiServiceUser)
        val userRepository = UserRepositoryImpl(userRemoteDataSource)
        val configuration = UseCase.Configuration(Dispatchers.IO)
        val getProfileUseCase = GetProfileUseCase(userRepository, configuration)
        val profileConverter = ProfileConverter()
        val context = this

        viewModel = ViewModelProvider(
            this,
            MainViewModelFactory(
                getProfileUseCase,
                profileConverter
            )
        )[MainViewModel::class]

        viewModel!!.loadProfile()

        lifecycleScope.launch {
            viewModel!!.profile.collect {
                when (it) {
                    is UiState.Loading -> {
                    }

                    is UiState.Error -> {
                        Log.d("main",it.errorMessage)
                        val intent = Intent(context, WelcomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                    is UiState.Success -> {
                        Log.d("main","ok")
                        val intent = if (sharedPref.getString("authToken", "") != "") {
                            Intent(context, CinemaActivity::class.java)
                        } else {
                            Intent(context, WelcomeActivity::class.java)
                        }
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }


    }

    companion object {
        private const val PREF = "shared_prefs_name"
    }
}