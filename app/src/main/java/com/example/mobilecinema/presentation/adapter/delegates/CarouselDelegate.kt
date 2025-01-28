package com.example.mobilecinema.presentation.adapter.delegates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.mobilecinema.R
import com.example.mobilecinema.data.model.movie.GenreModel
import com.example.mobilecinema.databinding.CarouselElementBinding
import com.example.mobilecinema.presentation.adapter.BaseViewHolder
import com.example.mobilecinema.presentation.adapter.Delegate
import com.example.mobilecinema.presentation.adapter.Item
import com.example.mobilecinema.presentation.adapter.holders.CarouselHolder
import com.example.mobilecinema.presentation.adapter.model.CarouselModel


class CarouselDelegate(
    private val genreOnClick: (Boolean,GenreModel) -> Unit,
    private val buttonOnClick: (String) -> Unit,
) : Delegate<CarouselElementBinding, CarouselModel> {

    override fun isRelativeItem(item: Item): Boolean = item is CarouselModel

    override fun getLayoutId(): Int = R.layout.carousel_element

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
    ): BaseViewHolder<CarouselElementBinding, CarouselModel> {
        val binding = CarouselElementBinding.inflate(layoutInflater, parent, false)
        return CarouselHolder(binding, genreOnClick, buttonOnClick)
    }

    override fun getDiffUtil(): DiffUtil.ItemCallback<CarouselModel> = diffUtil

    private val diffUtil = object : DiffUtil.ItemCallback<CarouselModel>() {
        override fun areItemsTheSame(oldItem: CarouselModel, newItem: CarouselModel) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: CarouselModel, newItem: CarouselModel) =
            oldItem == newItem
    }
}
