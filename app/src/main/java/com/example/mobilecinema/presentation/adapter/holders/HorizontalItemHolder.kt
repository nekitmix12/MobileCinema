package com.example.mobilecinema.presentation.adapter.holders

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilecinema.databinding.HorizontalRecycleViewBinding
import com.example.mobilecinema.presentation.adapter.AdapterWithDelegates
import com.example.mobilecinema.presentation.adapter.BaseViewHolder
import com.example.mobilecinema.presentation.adapter.Delegate
import com.example.mobilecinema.presentation.adapter.model.HorizontalItem

class HorizontalItemHolder(
    binding: HorizontalRecycleViewBinding,
    delegates: List<Delegate<*, *>>,
    outerDivider: Int,
) : BaseViewHolder<HorizontalRecycleViewBinding, HorizontalItem>(binding) {
    private val delegateAdapter = AdapterWithDelegates(delegates)

    init {
        with(binding.favoriteMoviesRecyclerView) {
            adapter = delegateAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        }
    }

    override fun onBinding(item: HorizontalItem) {
        super.onBinding(item)
        delegateAdapter.submitList(item.items)
    }
}
