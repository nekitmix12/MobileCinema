package com.example.mobilecinema.data.model.errors

import android.util.Log
import com.example.mobilecinema.R
import com.example.mobilecinema.data.StringHelper
import com.example.mobilecinema.data.datasource.ErrorParsable
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class LoginError(
    val message: String,
    @Transient private val stringHelper: StringHelper = StringHelper(),
) : ErrorParsable {
    override fun parse(): String{
        Log.d("error_is ",message)
       return when (message) {
            "Login failed" -> stringHelper.getString(R.string.login_failed)
            "User has already had review for this movie" -> stringHelper.getString(R.string.review_alreadyAdded)
            else -> message
        }
    }
}
