package com.example.mobilecinema.presentation.adapter.holders

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.annotation.LayoutRes
import com.example.mobilecinema.databinding.CommonFilmElementBinding
import com.example.mobilecinema.databinding.GridLayoutBinding
import com.example.mobilecinema.presentation.adapter.BaseViewHolder
import com.example.mobilecinema.presentation.adapter.model.FavoriteModel
import com.example.mobilecinema.presentation.adapter.model.GridItem
import com.example.mobilecinema.R

class GridHolder (
    binding: GridLayoutBinding,
    @LayoutRes
    val layoutRes:Int
) : BaseViewHolder<GridLayoutBinding, GridItem>(binding) {

    override fun onBinding(item: GridItem) = with(binding.gridLayout) {
        removeAllViews()


        for (i in item.items.indices) {
            val dynamicItem = item.items[i]
            val itemView = LayoutInflater.from(this.context).inflate(layoutRes, this, false)


            addView(itemView)
        }
    }

}