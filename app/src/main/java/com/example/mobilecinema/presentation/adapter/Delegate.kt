package com.example.mobilecinema.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.viewbinding.ViewBinding


interface Delegate<V : ViewBinding, I : Item> {
    fun isRelativeItem(item: Item): Boolean

    @LayoutRes //аннотация которая говорит что мы можем положить только ресурсы ,то есть мы не сможем положить просто число или ресурс ведущий не на разметку
    fun getLayoutId(): Int
    fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
    ): BaseViewHolder<V, I>
}