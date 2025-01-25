package com.example.mobilecinema.presentation.adapter.delegates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.mobilecinema.R
import com.example.mobilecinema.databinding.HorizontalRecycleViewBinding
import com.example.mobilecinema.presentation.adapter.BaseViewHolder
import com.example.mobilecinema.presentation.adapter.Delegate
import com.example.mobilecinema.presentation.adapter.Item
import com.example.mobilecinema.presentation.adapter.holders.HorizontalItemHolder
import com.example.mobilecinema.presentation.adapter.model.HorizontalItem

class HorizontalItemDelegates(
    private val delegatesList: List<Delegate<*, *>>,
    private val outerDivider: Int,
) : Delegate<HorizontalRecycleViewBinding, HorizontalItem> {
    override fun isRelativeItem(item: Item): Boolean = item is HorizontalItem

    override fun getLayoutId(): Int = R.layout.horizontal_recycle_view

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
    ): BaseViewHolder<HorizontalRecycleViewBinding, HorizontalItem> {
        val binding = HorizontalRecycleViewBinding.inflate(layoutInflater, parent, false)
        return HorizontalItemHolder(binding, delegatesList, outerDivider)
    }

    override fun getDiffUtil() = diffUtil

    private val diffUtil = object : DiffUtil.ItemCallback<HorizontalItem>() {
        override fun areItemsTheSame(oldItem: HorizontalItem, newItem: HorizontalItem) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: HorizontalItem, newItem: HorizontalItem) =
            oldItem == newItem

    }

}