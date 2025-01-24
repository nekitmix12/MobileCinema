package com.example.mobilecinema.data.model.review

import com.example.mobilecinema.data.model.auth.UserShortModel
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class ReviewModel (
    val id:String,
    val rating:Int,
    val reviewText:	String?,
    val isAnonymous: Boolean,
    val createDateTime:	String,
    val author: UserShortModel?
    )
{
}