package com.example.mobilecinema.presentation.adapter.delegates

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.mobilecinema.R
import com.example.mobilecinema.databinding.CarouselElementBinding
import com.example.mobilecinema.presentation.adapter.BaseViewHolder
import com.example.mobilecinema.presentation.adapter.Delegate
import com.example.mobilecinema.presentation.adapter.Item
import com.example.mobilecinema.presentation.adapter.model.CarouselModel


class CarouselDelegate : Delegate<CarouselElementBinding, CarouselModel> {
    override fun isRelativeItem(item: Item): Boolean = item is CarouselModel

    override fun getLayoutId(): Int = R.layout.carousel_element

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
    ): BaseViewHolder<CarouselElementBinding, CarouselModel> {
        val binding = CarouselElementBinding.inflate(layoutInflater,parent,false)
        return CarouselHolder(binding)
    }
}
