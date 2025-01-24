package com.example.mobilecinema.presentation.movies_details

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MoviesDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = ViewModelProvider(
            this, MoviesDetailViewModelFactory()
        )[MoviesDetailViewModel::class]

        val id = intent.getStringExtra("filmId")

        if (id != null) {
            lifecycleScope.launch {
                viewModel.init(id)
            }
        }
        setContent {
            MoviesDetailsScreen(viewModel)
        }

    }
}

