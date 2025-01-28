package com.example.mobilecinema.presentation.adapter.holders

import com.example.mobilecinema.R
import com.example.mobilecinema.databinding.MoviesFavoriteHeadingBinding
import com.example.mobilecinema.presentation.GradientHelper
import com.example.mobilecinema.presentation.adapter.BaseViewHolder
import com.example.mobilecinema.presentation.adapter.EventHandler
import com.example.mobilecinema.presentation.adapter.UiEvent
import com.example.mobilecinema.presentation.adapter.model.HeadingItem

class HeadingHolder(
    private val onClick:()->Unit,
    binding: MoviesFavoriteHeadingBinding,
) : BaseViewHolder<MoviesFavoriteHeadingBinding, HeadingItem>(binding) {
    override fun onBinding(item: HeadingItem) = with(binding) {
        textView.text = item.first
        textView.paint.shader = GradientHelper(textView.height.toFloat()).getLinearGradient(R.color.gradient_1,R.color.gradient_1)
        textView2.text = item.second
        textView2.setOnClickListener {
            onClick()
        }
    }
}