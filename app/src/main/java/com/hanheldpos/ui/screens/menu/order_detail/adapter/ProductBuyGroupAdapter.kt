package com.hanheldpos.ui.screens.menu.order_detail.adapter

import androidx.recyclerview.widget.DiffUtil
import com.diadiem.pos_components.enumtypes.TextHeaderEnum
import com.hanheldpos.R
import com.hanheldpos.databinding.ItemGroupChildOrderDetailViewBinding
import com.hanheldpos.model.order.ProductChosen
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder

data class ProductBuyGroup(
    val listProductChosen: List<ProductChosen>,
    val groupIndex: Int,
    val level: Int,
) {
    val parentId = listProductChosen.firstOrNull()?.Parent_id
    val parentName = listProductChosen.firstOrNull()?.ParentName
}

//
class ProductBuyGroupAdapter : BaseBindingListAdapter<ProductBuyGroup>(DiffCallBack()) {

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_group_child_order_detail_view
    }

    private lateinit var productBuyParentAdapter: ProductBuyParentAdapter

    override fun onBindViewHolder(holder: BaseBindingViewHolder<ProductBuyGroup>, position: Int) {
        val item = getItem(position)
        holder.bindItem(item)
        val binding = holder.binding as ItemGroupChildOrderDetailViewBinding

        if(item.level == 1) { // level of Requirement and Reward Title
            binding.tvProductTitle.setTextSize(TextHeaderEnum.H6)
            binding.tvQuantity.setTextSize(TextHeaderEnum.H6)
        } else {
            binding.tvProductTitle.setTextSize(TextHeaderEnum.H7)
            binding.tvQuantity.setTextSize(TextHeaderEnum.H7)
        }

        productBuyParentAdapter = ProductBuyParentAdapter()

        productBuyParentAdapter.submitList(item.listProductChosen.map {
            ProductBuyItem(
                it,
                level = item.level + 1
            )
        })
        binding.groupChildOrderDetailItem.adapter = productBuyParentAdapter
    }

    private class DiffCallBack : DiffUtil.ItemCallback<ProductBuyGroup>() {
        override fun areItemsTheSame(
            oldItem: ProductBuyGroup,
            newItem: ProductBuyGroup
        ): Boolean {
            return oldItem.parentId == newItem.parentId
        }

        override fun areContentsTheSame(
            oldItem: ProductBuyGroup,
            newItem: ProductBuyGroup
        ): Boolean {
            return oldItem == newItem
        }

    }
}