package com.example.mobilecinema.data.model.review

import kotlinx.serialization.Serializable

@Serializable
data class ReviewModifyModel(
    val reviewText:	String,
    val rating:	Int,
    val isAnonymous: Boolean
)