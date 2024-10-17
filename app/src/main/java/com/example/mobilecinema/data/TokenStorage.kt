package com.example.mobilecinema.data

import com.example.mobilecinema.data.model.auth.AuthToken

interface TokenStorage {

    fun save(token: String)

    fun getToken():String?

    fun clearToken()
}