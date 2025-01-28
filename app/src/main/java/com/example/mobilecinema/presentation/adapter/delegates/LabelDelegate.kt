package com.example.mobilecinema.presentation.adapter.delegates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.mobilecinema.R
import com.example.mobilecinema.databinding.LabelAllFilmsBinding
import com.example.mobilecinema.databinding.MoviesFavoriteHeadingBinding
import com.example.mobilecinema.presentation.adapter.BaseViewHolder
import com.example.mobilecinema.presentation.adapter.Delegate
import com.example.mobilecinema.presentation.adapter.Item
import com.example.mobilecinema.presentation.adapter.holders.HeadingHolder
import com.example.mobilecinema.presentation.adapter.holders.LabelHolder
import com.example.mobilecinema.presentation.adapter.model.HeadingItem
import com.example.mobilecinema.presentation.adapter.model.LabelModel

class LabelDelegate : Delegate<LabelAllFilmsBinding, LabelModel> {
    override fun isRelativeItem(item: Item): Boolean =
        item is LabelModel

    override fun getLayoutId(): Int = R.layout.label_all_films

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
    ): BaseViewHolder<LabelAllFilmsBinding, LabelModel> {
        val binding = LabelAllFilmsBinding.inflate(layoutInflater, parent, false)
        return LabelHolder(binding)
    }

    override fun getDiffUtil(): DiffUtil.ItemCallback<LabelModel> =
        object : DiffUtil.ItemCallback<LabelModel>() {
            override fun areItemsTheSame(oldItem: LabelModel, newItem: LabelModel) =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: LabelModel, newItem: LabelModel) =
                oldItem == newItem
        }

}