package com.example.mobilecinema.presentation.adapter

import android.graphics.Canvas
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

class FavoriteItemDecoration(
    private val scaleFactor: Float = 1.2f,
    private val scaleThreshold: Float = 0.5f
):RecyclerView.ItemDecoration() {
    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCount = parent.childCount
        val centerY = parent.height / 2f

        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams

            // Получаем координаты элемента
            val top = child.top - params.topMargin
            val bottom = child.bottom + params.bottomMargin
            val childCenterY = (top + bottom) / 2f

            // Расстояние от центра RecyclerView до центра элемента
            val distanceFromCenter = abs(centerY - childCenterY)

            // Нормализуем расстояние для расчета масштаба
            val scale = calculateScale(distanceFromCenter, parent.height, scaleFactor)

            // Применяем масштабирование
            child.scaleX = scale
            child.scaleY = scale
        }
    }

    // Расчет масштаба в зависимости от расстояния
    private fun calculateScale(distance: Float, parentHeight: Int, scaleFactor: Float): Float {
        val fraction = 1 - (distance / (parentHeight / 2f)).coerceIn(0f, 1f)
        return 1 + (fraction * (scaleFactor - 1))
    }
}