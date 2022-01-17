package com.hanheldpos.ui.screens.cart.adapter

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.order.settings.DiningOption
import com.hanheldpos.databinding.ItemDiningOptionBinding
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.base.adapter.BaseItemClickListener

class CartDiningOptionAdapter(private val onItemClickListener: BaseItemClickListener<DiningOption>, private var selectedIndex: Int = 0 ) :
    BaseBindingListAdapter<DiningOption>(DiffCallback(), onItemClickListener) {



    fun setSelectedIndex(index:Int){
        selectedIndex=index;
        notifyItemChanged(selectedIndex);
    }
    override fun getItemViewType(position: Int): Int {
        return R.layout.item_dining_option;
    }

    override fun submitList(list: MutableList<DiningOption>?) {
        super.submitList(list)
    }

    override fun onBindViewHolder(
        holder: BaseBindingViewHolder<DiningOption>,
        position: Int
    ) {
        val item: DiningOption = getItem(position);
        holder.bindItem(item);
        val binding = (holder.binding as ItemDiningOptionBinding);

        binding.diningOptionButton.apply {
            if (selectedIndex == position) {
                isChecked=true;
                setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.color_2
                    )
                )
            } else {
                isChecked=false;

                setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.color_6
                    )
                )
            }
            setOnClickListener {
                notifyItemChanged(selectedIndex);
                selectedIndex = position;
                notifyItemChanged(position);
                onItemClickListener.onItemClick(position,item);
            }
        };


    }

    private class DiffCallback : DiffUtil.ItemCallback<DiningOption>() {
        override fun areItemsTheSame(
            oldItem: DiningOption,
            newItem: DiningOption
        ): Boolean {
            return oldItem == newItem;
        }

        override fun areContentsTheSame(
            oldItem: DiningOption,
            newItem: DiningOption
        ): Boolean {
            return false
        }

    }
}