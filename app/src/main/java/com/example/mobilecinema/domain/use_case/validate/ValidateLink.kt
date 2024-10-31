package com.example.mobilecinema.domain.use_case.validate

import android.util.Patterns
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


class ValidateLink {
    fun execute(link: String?): ValidationResult {
        if (link==null)
            return ValidationResult(
                successful = true
            )
        if (link.isBlank()) {
            return ValidationResult(
                successful = false,
                errorManager = " The link can't be blank"
            )
        }
        if(!((link).endsWith(".jpg") || link.endsWith(".png") || link.endsWith(".gif")))
            return ValidationResult(
                successful = false,
                errorManager = "Link must be img"
            )
        if(!Patterns.WEB_URL.matcher(link).matches())
            return ValidationResult(
                successful = false,
                errorManager = "Link must be url"
            )
        try {
            val connection = URL(link).openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 5000
            connection.readTimeout = 5000
            connection.connect()
            if (connection.responseCode != HttpURLConnection.HTTP_OK)
                return ValidationResult(
                    successful = false,
                    errorManager = "Link must be relevant"
                )
        } catch (e: IOException) {
                return ValidationResult(
                    successful = false,
                    errorManager = "Link must be relevant"
                )
        }

        return ValidationResult(
            successful = true
        )
    }
}