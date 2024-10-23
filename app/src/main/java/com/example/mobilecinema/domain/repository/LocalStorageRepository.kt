package com.example.mobilecinema.domain.repository

interface LocalStorageRepository {
    fun save(token: String)

    fun getToken():String?

    fun clearToken()
}