package com.hanheldpos.ui.screens.cart.adapter

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.order.settings.DiningOptionItem
import com.hanheldpos.databinding.ItemDiningOptionBinding
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.base.adapter.BaseItemClickListener

class CartDiningOptionAdapter(private val onItemClickListener: BaseItemClickListener<DiningOptionItem>,private var selectedIndex: Int = 0 ) :
    BaseBindingListAdapter<DiningOptionItem>(DiffCallback(), onItemClickListener) {



    fun setSelectedIndex(index:Int){
        selectedIndex=index;
        notifyItemChanged(selectedIndex);
    }
    override fun getItemViewType(position: Int): Int {
        return R.layout.item_dining_option;
    }

    override fun submitList(list: MutableList<DiningOptionItem>?) {
        super.submitList(list)
    }

    override fun onBindViewHolder(
        holder: BaseBindingViewHolder<DiningOptionItem>,
        position: Int
    ) {
        val item: DiningOptionItem = getItem(position);
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

    class DiffCallback : DiffUtil.ItemCallback<DiningOptionItem>() {
        override fun areItemsTheSame(
            oldItem: DiningOptionItem,
            newItem: DiningOptionItem
        ): Boolean {
            return oldItem == newItem;
        }

        override fun areContentsTheSame(
            oldItem: DiningOptionItem,
            newItem: DiningOptionItem
        ): Boolean {
            return false
        }

    }
}