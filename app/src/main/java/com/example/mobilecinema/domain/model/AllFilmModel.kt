package com.example.mobilecinema.domain.model

import android.graphics.Bitmap

data class AllFilmModel(
    val url:Bitmap?,
    val rating:Float,
    val isLiked:Boolean
)
