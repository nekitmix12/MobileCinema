package com.example.mobilecinema.presentation.adapter.delegates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.mobilecinema.R
import com.example.mobilecinema.databinding.MoviesFavoriteHeadingBinding
import com.example.mobilecinema.presentation.adapter.BaseViewHolder
import com.example.mobilecinema.presentation.adapter.Delegate
import com.example.mobilecinema.presentation.adapter.EventHandler
import com.example.mobilecinema.presentation.adapter.Item
import com.example.mobilecinema.presentation.adapter.UiEvent
import com.example.mobilecinema.presentation.adapter.holders.HeadingHolder
import com.example.mobilecinema.presentation.adapter.model.HeadingItem

class HeadingDelegate : Delegate<MoviesFavoriteHeadingBinding, HeadingItem> {
    override fun isRelativeItem(item: Item): Boolean =
        item is HeadingItem

    override fun getLayoutId(): Int = R.layout.movies_favorite_heading

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
    ): BaseViewHolder<MoviesFavoriteHeadingBinding, HeadingItem> {
        val binding = MoviesFavoriteHeadingBinding.inflate(layoutInflater, parent, false)
        return HeadingHolder(binding)
    }

    override fun getDiffUtil(): DiffUtil.ItemCallback<HeadingItem> =
        object : DiffUtil.ItemCallback<HeadingItem>() {
            override fun areItemsTheSame(oldItem: HeadingItem, newItem: HeadingItem) =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: HeadingItem, newItem: HeadingItem) =
                oldItem == newItem
        }

}