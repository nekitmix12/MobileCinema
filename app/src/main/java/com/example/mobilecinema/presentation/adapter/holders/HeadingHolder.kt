package com.example.mobilecinema.presentation.adapter.holders

import com.example.mobilecinema.databinding.MoviesFavoriteHeadingBinding
import com.example.mobilecinema.presentation.adapter.BaseViewHolder
import com.example.mobilecinema.presentation.adapter.EventHandler
import com.example.mobilecinema.presentation.adapter.UiEvent
import com.example.mobilecinema.presentation.adapter.model.HeadingItem

class HeadingHolder(
    binding: MoviesFavoriteHeadingBinding,
) : BaseViewHolder<MoviesFavoriteHeadingBinding, HeadingItem>(binding) {
    override fun onBinding(item: HeadingItem) = with(binding) {
        textView.text = item.first
        textView2.text = item.second
        textView2.setOnClickListener {
            //eventHandler.handle(UiEvent.ButtonAllClicked)
        }
    }
}