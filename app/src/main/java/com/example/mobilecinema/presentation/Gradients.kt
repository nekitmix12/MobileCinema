package com.example.mobilecinema.presentation

import android.graphics.LinearGradient
import android.graphics.Shader
import com.example.mobilecinema.di.MainContext

class GradientHelper(private val height: Float = 0f, private val weight: Float = 0f) {

    fun getLinearGradient(firstColorRes: Int, secondColorRes: Int): LinearGradient = LinearGradient(
        0f, 0f, 0f, height,
        MainContext.provideInstance().provideContext().getColor(firstColorRes),
        MainContext.provideInstance().provideContext().getColor(secondColorRes),
        Shader.TileMode.CLAMP
    )
}