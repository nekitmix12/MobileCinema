package com.example.mobilecinema.presentation.adapter

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseViewHolder<out V:ViewBinding,in I:Item>(
    val binding: V
):RecyclerView.ViewHolder(binding.root) {
    abstract fun onBinding(item: I)
}