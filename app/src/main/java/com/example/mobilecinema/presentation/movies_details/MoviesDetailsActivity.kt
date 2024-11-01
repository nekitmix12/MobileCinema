package com.example.mobilecinema.presentation.movies_details

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class MoviesDetailsActivity:ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            MoviesDetailsScreen()
        }
    }
}

