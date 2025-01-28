package com.example.mobilecinema.presentation.adapter.delegates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.mobilecinema.R
import com.example.mobilecinema.databinding.MoviesElementAllFilmsBinding

import com.example.mobilecinema.presentation.adapter.BaseViewHolder
import com.example.mobilecinema.presentation.adapter.Delegate
import com.example.mobilecinema.presentation.adapter.Item
import com.example.mobilecinema.presentation.adapter.holders.AllFIlmHolder
import com.example.mobilecinema.presentation.adapter.holders.ButtonHolder
import com.example.mobilecinema.presentation.adapter.model.AllFilmModel


class AllFilmDelegate(private val onClicked: (String) -> Unit) :
    Delegate<MoviesElementAllFilmsBinding, AllFilmModel> {

    override fun isRelativeItem(item: Item): Boolean = item is AllFilmModel

    override fun getLayoutId(): Int = R.layout.movies_element_all_films

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
    ): BaseViewHolder<MoviesElementAllFilmsBinding, AllFilmModel> {
        val binding = MoviesElementAllFilmsBinding.inflate(layoutInflater, parent, false)
        return AllFIlmHolder(binding, onClicked)
    }

    override fun getDiffUtil(): DiffUtil.ItemCallback<AllFilmModel> = diffUtil

    private val diffUtil = object : DiffUtil.ItemCallback<AllFilmModel>() {
        override fun areItemsTheSame(oldItem: AllFilmModel, newItem: AllFilmModel) =
            true

        override fun areContentsTheSame(oldItem: AllFilmModel, newItem: AllFilmModel) =
            true
    }
}