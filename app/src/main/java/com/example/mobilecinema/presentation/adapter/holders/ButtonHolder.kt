package com.example.mobilecinema.presentation.adapter.holders

import com.example.mobilecinema.databinding.RandomButtonBinding
import com.example.mobilecinema.presentation.adapter.BaseViewHolder
import com.example.mobilecinema.presentation.adapter.model.ButtonModel

class ButtonHolder(binding: RandomButtonBinding, private val onClicked: () -> Unit) :
    BaseViewHolder<RandomButtonBinding, ButtonModel>(binding) {


    override fun onBinding(item: ButtonModel) = with(binding) {
        randomFilmButton.setOnClickListener { onClicked }
    }
}