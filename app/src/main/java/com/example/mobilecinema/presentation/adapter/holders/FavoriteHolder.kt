package com.example.mobilecinema.presentation.adapter.holders

import com.example.mobilecinema.databinding.CommonFilmElementBinding
import com.example.mobilecinema.presentation.adapter.BaseViewHolder
import com.example.mobilecinema.presentation.adapter.model.FavoriteModel

class FavoriteHolder(
    binding: CommonFilmElementBinding,
) : BaseViewHolder<CommonFilmElementBinding, FavoriteModel>(binding) {

    override fun onBinding(item: FavoriteModel) = with(binding) {

        commonFilmElementImage.setImageBitmap(item.image)
    }

}