package com.example.mobilecinema.domain.model

data class ReviewModifyModel(
    val reviewText:String,
    val rating:Int,
    val isAnonymous:Boolean
)
