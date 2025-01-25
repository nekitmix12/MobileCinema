package com.example.mobilecinema.presentation.adapter.delegates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.mobilecinema.R
import com.example.mobilecinema.databinding.CommonFilmElementBinding
import com.example.mobilecinema.presentation.adapter.BaseViewHolder
import com.example.mobilecinema.presentation.adapter.Delegate
import com.example.mobilecinema.presentation.adapter.Item
import com.example.mobilecinema.presentation.adapter.holders.FavoriteHolder
import com.example.mobilecinema.presentation.adapter.model.FavoriteModel

class FavoriteDelegate : Delegate<CommonFilmElementBinding, FavoriteModel> {

    override fun isRelativeItem(item: Item): Boolean = item is FavoriteModel

    override fun getLayoutId(): Int = R.layout.common_film_element

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
    ): BaseViewHolder<CommonFilmElementBinding, FavoriteModel> {
        val binding = CommonFilmElementBinding.inflate(layoutInflater, parent, false)
        return FavoriteHolder(binding)
    }

    override fun getDiffUtil(): DiffUtil.ItemCallback<FavoriteModel> = diffUtil

    private val diffUtil = object : DiffUtil.ItemCallback<FavoriteModel>() {
        override fun areItemsTheSame(oldItem: FavoriteModel, newItem: FavoriteModel) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: FavoriteModel, newItem: FavoriteModel) =
            oldItem == newItem
    }
}