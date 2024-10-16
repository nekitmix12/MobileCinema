package com.example.mobilecinema.data.model

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class ReviewModel (
    val id:String,
    val rating:Int,
    val reviewText:	String?,
    val isAnonymous: Boolean,
    val createDateTime:	Instant,
    val author: UserShortModel
    )
{
    init{
        require(try {
            UUID.fromString(id)
            true
        } catch (e: IllegalArgumentException) {
            false
        })
    }
}