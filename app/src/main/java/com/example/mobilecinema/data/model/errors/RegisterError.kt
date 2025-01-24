package com.example.mobilecinema.data.model.errors

import com.example.mobilecinema.R
import com.example.mobilecinema.data.StringHelper
import com.example.mobilecinema.data.datasource.ErrorParsable
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class RegisterError(
    val message: String,
    val errors: Map<String, ValidationError>,
    @Transient private val stringHelper: StringHelper = StringHelper()
) : ErrorParsable {


    override fun parse(): String {
        val errorValues = errors.values
        if (errorValues.isEmpty()) {
            return stringHelper.getString(R.string.strange_request_error)
        }

        errorValues.forEach { error ->
            error.errors.forEach { detail ->
                detail?.let {
                    val param = stringHelper.getTextInsideQuotes(it.errorMessage)
                    if (param != null) {
                        return stringHelper.getString(R.string.name_validation_error)
                    }
                }
            }
        }
        return stringHelper.getString(R.string.strange_request_error)
    }
}
