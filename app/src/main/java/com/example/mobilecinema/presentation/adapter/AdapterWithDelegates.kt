package com.example.mobilecinema.presentation.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.mobilecinema.presentation.adapter.delegates.CarouselDelegate


class AdapterWithDelegates(
    private val delegate: List<Delegate<*, *>>,
) : ListAdapter<Item, BaseViewHolder<ViewBinding, Item>>(DelegateDiffUtil(delegate)) {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): BaseViewHolder<ViewBinding, Item> {
        val inflater = LayoutInflater.from(parent.context)
        return delegate.find { it.getLayoutId() == viewType }
            ?.getViewHolder(inflater, parent)
            ?.let { it as BaseViewHolder<ViewBinding, Item> }
            ?: throw IllegalArgumentException("View type not found: $viewType")
    }

    override fun onBindViewHolder(holder: BaseViewHolder<ViewBinding, Item>, position: Int) {
        holder.onBinding(currentList[position])
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<ViewBinding, Item>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            holder.onBinding(currentList[position], payloads)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = currentList[position]
        return delegate.find { it.isRelativeItem(item) }
            ?.getLayoutId()
            ?: throw IllegalArgumentException("View type not found: $item")
    }

}