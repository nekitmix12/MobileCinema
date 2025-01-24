package com.example.mobilecinema.domain.model

data class ReviewModel(
    val id:String,
    val rating:Int,
    val reviewText:String?,
    val isAnonymous:Boolean,
    val createTime:String,
    val author:UserShortModel
)