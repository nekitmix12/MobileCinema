package com.example.mobilecinema.presentation.adapter.holders

import com.example.mobilecinema.R
import com.example.mobilecinema.databinding.LabelAllFilmsBinding
import com.example.mobilecinema.presentation.GradientHelper
import com.example.mobilecinema.presentation.adapter.BaseViewHolder
import com.example.mobilecinema.presentation.adapter.model.LabelModel

class LabelHolder(binding: LabelAllFilmsBinding) :
    BaseViewHolder<LabelAllFilmsBinding, LabelModel>(binding) {
    override fun onBinding(item: LabelModel) = with(binding) {
        allFilms.text = item.label
        allFilms.paint.shader = GradientHelper(allFilms.height.toFloat()).getLinearGradient(
            R.color.gradient_1,
            R.color.gradient_1
        )

    }
}