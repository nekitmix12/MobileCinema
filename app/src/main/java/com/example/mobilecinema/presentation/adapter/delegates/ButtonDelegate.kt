package com.example.mobilecinema.presentation.adapter.delegates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.mobilecinema.R
import com.example.mobilecinema.databinding.RandomButtonBinding
import com.example.mobilecinema.presentation.adapter.BaseViewHolder
import com.example.mobilecinema.presentation.adapter.Delegate
import com.example.mobilecinema.presentation.adapter.Item
import com.example.mobilecinema.presentation.adapter.holders.ButtonHolder
import com.example.mobilecinema.presentation.adapter.model.ButtonModel

class ButtonDelegate(private val onClicked: () -> Unit) :
    Delegate<RandomButtonBinding, ButtonModel> {

    override fun isRelativeItem(item: Item): Boolean = item is ButtonModel

    override fun getLayoutId(): Int = R.layout.random_button

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
    ): BaseViewHolder<RandomButtonBinding, ButtonModel> {
        val binding = RandomButtonBinding.inflate(layoutInflater, parent, false)
        return ButtonHolder(binding, onClicked)
    }

    override fun getDiffUtil(): DiffUtil.ItemCallback<ButtonModel> = diffUtil

    private val diffUtil = object : DiffUtil.ItemCallback<ButtonModel>() {
        override fun areItemsTheSame(oldItem: ButtonModel, newItem: ButtonModel) =
            true

        override fun areContentsTheSame(oldItem: ButtonModel, newItem: ButtonModel) =
            true
    }
}