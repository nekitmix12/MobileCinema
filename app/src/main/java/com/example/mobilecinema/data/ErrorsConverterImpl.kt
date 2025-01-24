package com.example.mobilecinema.data

import android.util.Log
import com.example.mobilecinema.R
import com.example.mobilecinema.data.datasource.ErrorParsable
import com.example.mobilecinema.data.model.errors.LoginError
import com.example.mobilecinema.data.model.errors.RegisterError
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import okhttp3.ResponseBody

class ErrorsConverterImpl(
    private val stringHelper: StringHelper = StringHelper(),
    private val format: Json = Json {
        ignoreUnknownKeys = false
        isLenient = false
    },
    private val possibleTypes: List<Class<out ErrorParsable>> =
        listOf(
            RegisterError::class.java,
            LoginError::class.java
        ),
) : ErrorsConverter {


    override fun invokeMethod(responseBody: ResponseBody?, responseCode: Int): String {
        return try {
            Log.d("code",responseCode.toString())
            when (responseCode) {
                401 -> stringHelper.getString(R.string.unauthorized)

                400 -> {
                    val errorJson = responseBody?.string()
                        ?: throw IllegalArgumentException("Response body is null")
                    val errorMessage = parseError(errorJson, possibleTypes)
                    errorMessage ?: throw IllegalArgumentException("Unable to parse error")
                }

                else -> throw IllegalArgumentException("Unhandled response code: $responseCode")
            }
        } catch (e: Exception) {
            stringHelper.getString(R.string.strange_request_error)
        }
    }

    @OptIn(InternalSerializationApi::class)
    private fun parseError(json: String, types: List<Class<out ErrorParsable>>): String? {
        types.forEach { type ->
            try {
                val kClass = type.kotlin
                val serializer = kClass.serializer()
                val parsedError = format.decodeFromString(serializer, json)
                return parsedError.parse()
            } catch (e: Exception) {
                stringHelper.getString(R.string.strange_request_error)
            }
        }
        return null
    }


}