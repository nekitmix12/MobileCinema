package com.example.mobilecinema.presentation.adapter.holders

import android.annotation.SuppressLint
import android.view.View
import com.example.mobilecinema.databinding.MoviesElementAllFilmsBinding
import com.example.mobilecinema.di.MainContext
import com.example.mobilecinema.presentation.ColorHelper
import com.example.mobilecinema.presentation.adapter.BaseViewHolder
import com.example.mobilecinema.presentation.adapter.model.AllFilmModel
import com.example.mobilecinema.presentation.adapter.model.ButtonModel

class AllFIlmHolder(binding: MoviesElementAllFilmsBinding, private val onClicked: (String) -> Unit) :
BaseViewHolder<MoviesElementAllFilmsBinding, AllFilmModel>(binding) {


    override fun onBinding(item: AllFilmModel) = with(binding) {
        ratingContainer.backgroundTintList = MainContext.provideInstance().provideContext().getColorStateList(ColorHelper.getColor(item.rating.toInt()))
        allFilmsElement.setImageBitmap(item.img)
        allFilmsElement.setOnClickListener { onClicked(item.movieId) }
        rating.text = item.rating.toString()
        favoriteMoviesAllFilms.visibility = if(item.isFavorite) View.VISIBLE else View.GONE
    }
}