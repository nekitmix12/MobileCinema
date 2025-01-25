package com.example.mobilecinema.presentation.adapter

interface EventHandler<T> {
    fun handle(event: T)
}