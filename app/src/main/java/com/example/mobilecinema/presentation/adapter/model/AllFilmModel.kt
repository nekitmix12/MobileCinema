package com.example.mobilecinema.presentation.adapter.model

import android.graphics.Bitmap
import com.example.mobilecinema.presentation.adapter.Item

data class AllFilmModel(
    val img:Bitmap?,
    val rating:Float,
    val isFavorite:Boolean,
    val movieId:String,
): Item
