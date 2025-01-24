package com.example.mobilecinema.presentation.adapter.delegates

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.TextViewCompat
import com.example.mobilecinema.R
import com.example.mobilecinema.databinding.CarouselElementBinding
import com.example.mobilecinema.presentation.adapter.BaseViewHolder
import com.example.mobilecinema.presentation.adapter.model.CarouselModel
import com.google.android.flexbox.FlexboxLayout
import com.squareup.picasso.Picasso

class CarouselHolder(
    binding: CarouselElementBinding,
) : BaseViewHolder<CarouselElementBinding, CarouselModel>(binding) {
    override fun onBinding(item: CarouselModel) = with(binding) {
        item.addGenre(flexboxLayout)
        item.loadImg(carouselImage)
        CarouselCardName.text = item.name
    }

    private fun CarouselModel.loadImg(img: ImageView) {
        Picasso.get().load(this.poster).error(R.drawable.icon_background)
            .into(img)
    }

    private fun CarouselModel.addGenre(layout: FlexboxLayout) {
        /*layout.removeAllViews()
        this.genres.forEachIndexed { index, genre ->

            if (index > 2)
                return@forEachIndexed

            val textView = TextView(layout.context).apply {
                text = genre.first
                isSelected = genre.second
                TextViewCompat.setTextAppearance(this, R.style.CarouselText)

                layoutParams = FlexboxLayout.LayoutParams(
                    FlexboxLayout.LayoutParams.WRAP_CONTENT,
                    FlexboxLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    marginStart = 24.dpToPx(context)
                    bottomMargin = 8.dpToPx(context)
                }
                elevation = 1f.dpToPx(context)
            }
            layout.addView(textView)
        }*/
        while (layout.childCount > this.genres.size) {
            layout.removeViewAt(layout.childCount - 1)
        }

        this.genres.forEachIndexed { index, genre ->
            if (index > 2) return@forEachIndexed

            val textView: TextView = if (index < layout.childCount) {
                layout.getChildAt(index) as TextView
            } else {
                TextView(layout.context).apply {
                    layout.addView(this)
                }
            }

            // Обновляем содержимое TextView
            textView.text = genre.first
            textView.isSelected = genre.second
            TextViewCompat.setTextAppearance(textView, R.style.CarouselText)

            (textView.layoutParams as FlexboxLayout.LayoutParams).apply {
                marginStart = 24.dpToPx(layout.context)
                bottomMargin = 8.dpToPx(layout.context)
            }
            textView.elevation = 1f.dpToPx(layout.context)
        }
    }
}

private fun Int.dpToPx(context: Context): Int {
    return (this * context.resources.displayMetrics.density).toInt()
}

private fun Float.dpToPx(context: Context): Float {
    return (this * context.resources.displayMetrics.density)
}
