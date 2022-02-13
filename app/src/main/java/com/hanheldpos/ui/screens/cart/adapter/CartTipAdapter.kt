package com.hanheldpos.ui.screens.cart.adapter

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.databinding.ItemCartTipBinding
import com.hanheldpos.model.cart.fee.FeeTip
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.base.adapter.BaseItemClickListener

class CartTipAdapter(
    private val onItemClickListener: BaseItemClickListener<FeeTip>,
    private var selectedIndex: Int = 0
) : BaseBindingListAdapter<FeeTip>(DiffCallback(), onItemClickListener) {

    fun setSelectedIndex(index: Int) {
        selectedIndex = index
        notifyItemChanged(selectedIndex)
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_cart_tip
    }

    override fun onBindViewHolder(
        holder: BaseBindingViewHolder<FeeTip>,
        position: Int
    ) {
        val item: FeeTip = getItem(position)
        holder.bindItem(item)
        val binding = (holder.binding as ItemCartTipBinding)

        binding.tipButton.apply {
            if (selectedIndex == position) {
                isChecked = true
                setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.color_0
                    )
                )
            } else {
                isChecked = false
                setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.color_5
                    )
                )
            }
            setOnClickListener {
                notifyItemChanged(selectedIndex)
                selectedIndex = position
                notifyItemChanged(position)
                onItemClickListener.onItemClick(position, item)
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<FeeTip>() {
        override fun areItemsTheSame(
            oldItem: FeeTip,
            newItem: FeeTip
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: FeeTip,
            newItem: FeeTip
        ): Boolean {
            return false
        }

    }
}