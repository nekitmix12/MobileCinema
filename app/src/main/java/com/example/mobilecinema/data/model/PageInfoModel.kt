package com.example.mobilecinema.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PageInfoModel (
    val pageSize: Int,
    val pageCount: Int,
    val currentPage: Int
)