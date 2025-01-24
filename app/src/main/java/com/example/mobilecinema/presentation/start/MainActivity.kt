package com.example.mobilecinema.presentation.start

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.mobilecinema.R
import com.example.mobilecinema.di.MainContext
import com.example.mobilecinema.domain.use_case.UiState
import com.example.mobilecinema.presentation.CinemaActivity
import com.example.mobilecinema.presentation.WelcomeActivity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.splash_screen)


        MainContext.init(this)

        viewModel = ViewModelProvider(
            this, MainViewModelFactory()
        )[MainViewModel::class]

        val context = this

        viewModel.findToken()

        lifecycleScope.launch {
            viewModel.find.collect {
                when (it) {
                    true -> viewModel.checkToken()

                    false -> {
                        val intent = Intent(context, WelcomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                    null -> {}
                }
            }
        }

        lifecycleScope.launch {
            viewModel.profile.collect {
                when (it) {
                    is UiState.Loading -> {}

                    is UiState.Error -> {
                        startActivity(
                            Intent(
                                context,
                                WelcomeActivity::class.java
                            ).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                        )
                        finish()
                    }

                    is UiState.Success -> {

                        startActivity(
                            Intent(
                                context,
                                CinemaActivity::class.java
                            ).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                        )
                        finish()
                    }
                }
            }
        }
    }

}



