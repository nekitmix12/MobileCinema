package com.example.mobilecinema.presentation.custom_class

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.unit.Dp
import androidx.core.content.ContextCompat
import com.example.mobilecinema.R

@SuppressLint("CustomViewStyleable")
class TopCropImageView  @JvmOverloads constructor(
    context: Context, attrs: AttributeSet?
    , defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var cornerRadius: Float = 0f
    private lateinit var bitmap: Bitmap

    private val paint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.dark_faded)
        style = Paint.Style.FILL
    }

    fun setImage(bitmap: Bitmap) {
        this.bitmap = bitmap
        invalidate()
    }

    init {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.CustomView, 0, 0)
            try {
                cornerRadius = typedArray.getDimension(R.styleable.CustomView_cornerRadius, 0f)
                val resourcesId = typedArray.getResourceId(R.styleable.CustomView_src,0)
                if (resourcesId !=0) {
                    bitmap = BitmapFactory.decodeResource(resources, resourcesId)
                }
            } finally {
                typedArray.recycle()
            }
        }

    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        clipCanvas(width.toFloat(),height.toFloat(),canvas)
        setCorrectImage(canvas, bitmap)
    }

    private fun clipCanvas(w: Float, h: Float, canvas: Canvas){
        val path = Path()
        path.moveTo(0f, 0f)
        path.lineTo(0f, h-cornerRadius)
        path.quadTo(0f, h,cornerRadius, h)
        path.lineTo(w-cornerRadius, h)
        path.quadTo(w, h, w , h-cornerRadius)
        path.lineTo(w, 0f)
        path.lineTo(0f, 0f)
        path.close()
        canvas.clipPath(path)

        bitmap.let{

        }
    }

    private fun setCorrectImage(canvas: Canvas, bitmap: Bitmap){
        val bitmapWidth = bitmap.width
        val bitmapHeight = bitmap.height

        val viewWidth = width.toFloat()
        val viewHeight = height.toFloat()

        val scale = maxOf(viewWidth / bitmapWidth, viewHeight / bitmapHeight)
        matrix.postScale(scale, scale)

        canvas.drawBitmap(bitmap, 0f, 0f, null)
    }
}