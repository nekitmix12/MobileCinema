package com.example.mobilecinema.presentation.adapter.delegates

import android.content.Context
import android.util.TypedValue
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.example.mobilecinema.R
import com.example.mobilecinema.data.model.movie.GenreModel
import com.example.mobilecinema.databinding.CarouselElementBinding
import com.example.mobilecinema.presentation.adapter.BaseViewHolder
import com.example.mobilecinema.presentation.adapter.model.CarouselModel
import com.google.android.flexbox.FlexboxLayout
import com.squareup.picasso.Picasso

class CarouselHolder(
    binding: CarouselElementBinding,
    //itemClickedListener:(GenreModel)->Unit
) : BaseViewHolder<CarouselElementBinding, CarouselModel>(binding) {
    override fun onBinding(item: CarouselModel,) = with(binding) {
        item.addGenre(flexboxLayout)
        item.loadImg(carouselImage)
        CarouselCardName.text = item.name
    }

    private fun CarouselModel.loadImg(img: ImageView) {
        Picasso.get().load(this.poster).error(R.drawable.icon_background)
            .into(img)
    }

    private fun CarouselModel.addGenre(layout: FlexboxLayout) {
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

            textView.text = genre.first
            textView.isSelected = genre.second

            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            textView.setTextColor(ContextCompat.getColor(layout.context, R.color.white))
            textView.typeface = ResourcesCompat.getFont(layout.context, R.font.manrope_bold)

            textView.setPadding(
                12.dpToPx(layout.context),
                0.dpToPx(layout.context),
                12.dpToPx(layout.context),
                4.dpToPx(layout.context)
            )

            textView.background =
                ContextCompat.getDrawable(layout.context, R.drawable.registration_selector_button)

            val layoutParams = textView.layoutParams as FlexboxLayout.LayoutParams
            layoutParams.marginStart = 2.dpToPx(layout.context)
            layoutParams.marginEnd = 2.dpToPx(layout.context)
            layoutParams.bottomMargin = 2.dpToPx(layout.context)
            layoutParams.topMargin = 2.dpToPx(layout.context)
            textView.layoutParams = layoutParams
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
