package com.example.mobilecinema.presentation.custom_class

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

class RoundedImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        isAntiAlias = true
    }
    private var path: Path? = null

    override fun onDraw(canvas: Canvas) {
        if (path == null) {
            path = Path().apply {
                addRoundRect(
                    RectF(0f, 0f, width.toFloat(), height.toFloat()),
                    50f, // радиус углов
                    50f, // радиус углов
                    Path.Direction.CW
                )
            }
        }
        canvas.clipPath(path!!)
        super.onDraw(canvas)
    }
}