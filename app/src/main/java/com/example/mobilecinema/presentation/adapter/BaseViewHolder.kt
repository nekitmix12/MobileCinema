package com.example.mobilecinema.presentation.adapter

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseViewHolder<out V:ViewBinding,in I:Item>(
    val binding: V
):RecyclerView.ViewHolder(binding.root) {
    lateinit var item: @UnsafeVariance I
    open fun onBinding(item: I){
        this.item = item
    }
    open fun onBinding(item: I,payloads: MutableList<Any>){
        this.item = item
    }
}