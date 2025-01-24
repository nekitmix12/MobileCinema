package com.example.mobilecinema.data.model.errors

import kotlinx.serialization.Serializable

@Serializable
data class ValidationError(
    val rawView:String?,
    val attemptedValue:String?,
    val errors: List<SimpleError?>,
    val validationState: Int?,
    val isContainerNode: Boolean?,
    val children: String?
)
