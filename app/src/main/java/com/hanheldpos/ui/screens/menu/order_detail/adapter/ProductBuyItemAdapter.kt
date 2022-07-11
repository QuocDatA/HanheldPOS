package com.hanheldpos.ui.screens.menu.order_detail.adapter

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import com.diadiem.pos_components.enumtypes.TextHeaderEnum
import com.hanheldpos.R
import com.hanheldpos.databinding.ItemProductOrderDetailViewBinding
import com.hanheldpos.model.order.ProductChosen
import com.hanheldpos.model.product.ProductType
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder

data class ProductBuyItem(
    val chosenProduct: ProductChosen,
    val level: Int,
) {
    val isRoot = level == 0
}

class ProductBuyParentAdapter : BaseBindingListAdapter<ProductBuyItem>(DiffCallBack()) {

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_product_order_detail_view
    }

    private lateinit var productBuyGroupAdapter: ProductBuyGroupAdapter

    override fun onBindViewHolder(holder: BaseBindingViewHolder<ProductBuyItem>, position: Int) {
        val item = getItem(position)
        holder.bindItem(item)
        val binding = holder.binding as ItemProductOrderDetailViewBinding

        productBuyGroupAdapter = ProductBuyGroupAdapter()

        val chosenProduct = item.chosenProduct

        setHeader(holder,item.level)

        if((item.chosenProduct.DiscountTotalPrice ?: 0.0) <= 0.0) {
            binding.discountDetailContainer.visibility = View.GONE
        }

        if (chosenProduct.ProductTypeId == ProductType.BUNDLE.value || chosenProduct.ProductTypeId == ProductType.BUYX_GETY_DISC.value) {

            if ((chosenProduct.ProductChoosedList?.isNotEmpty() == true)) {

                var groupIndex = 0
                val productGroup = chosenProduct.ProductChoosedList
                    ?.filter { it._id != null }
                    ?.groupBy {

                        if (chosenProduct.ProductTypeId == ProductType.BUNDLE.value)
                            it.Parent_id
                        else
                            it.ProductApplyTo
                    }

                val products = productGroup?.map {
                    groupIndex++
                    ProductBuyGroup(
                        listProductChosen = it.value,
                        groupIndex = groupIndex,
                        level = item.level + 1,
                    )
                }

                productBuyGroupAdapter.submitList(products)
                binding.productGroupRecyclerView.adapter = productBuyGroupAdapter
            }
        }
    }

    private fun setHeader(holder: BaseBindingViewHolder<ProductBuyItem>, level: Int) {
        val binding = holder.binding as ItemProductOrderDetailViewBinding
        when(level) {
            0 -> {
                binding.topSpacer.visibility = View.VISIBLE
            }
            2 -> { // Level of bundle group inside combo and buy_x_get_y
                binding.tvProductTitle.setTextSize(TextHeaderEnum.H7)
                binding.tvQuantity.setTextSize(TextHeaderEnum.H7)
                binding.totalProduct.setTextSize(TextHeaderEnum.H7)
                binding.totalProductAddOn.setTextSize(TextHeaderEnum.H7)
            }
            4 -> {
                binding.tvProductTitle.setTextSize(TextHeaderEnum.H7)
                binding.tvQuantity.setTextSize(TextHeaderEnum.H7)
                binding.totalProduct.setTextSize(TextHeaderEnum.H7)
                binding.totalProductAddOn.setTextSize(TextHeaderEnum.H7)
            }
        }
    }

    private class DiffCallBack : DiffUtil.ItemCallback<ProductBuyItem>() {
        override fun areItemsTheSame(
            oldItem: ProductBuyItem,
            newItem: ProductBuyItem
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: ProductBuyItem,
            newItem: ProductBuyItem
        ): Boolean {
            return oldItem == newItem
        }

    }
}