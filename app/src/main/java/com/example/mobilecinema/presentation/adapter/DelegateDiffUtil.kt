package com.example.mobilecinema.presentation.adapter

import androidx.recyclerview.widget.DiffUtil

class DelegateDiffUtil(
    private val delegates: List<Delegate<*,*>>
):DiffUtil.ItemCallback<Item>() {
    override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean =
        if (oldItem::class != newItem::class) false
    else
            getItemCallBack(oldItem).areItemsTheSame(oldItem,newItem)


    override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean =
        if (oldItem::class != newItem::class) false
        else
            getItemCallBack(oldItem).areContentsTheSame(oldItem,newItem)

    override fun getChangePayload(oldItem: Item, newItem: Item): Any? =
        if (oldItem::class != newItem::class) false
    else
        getItemCallBack(oldItem).getChangePayload(oldItem,newItem)


    private fun getItemCallBack(item:Item):DiffUtil.ItemCallback<Item> = delegates.find { it.isRelativeItem(item) }
        ?.getDiffUtil()
        ?.let { it as DiffUtil.ItemCallback<Item> }
        ?: throw IllegalArgumentException("DiffUtil now found $item")
}