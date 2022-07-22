package com.hanheldpos.ui.screens.product.buy_x_get_y.adapter

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import com.google.android.material.tabs.TabLayout
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.discount.CustomerBuys
import com.hanheldpos.data.api.pojo.product.Product
import com.hanheldpos.databinding.ItemBuyXGetYGroupBinding
import com.hanheldpos.model.cart.BaseProductInCart
import com.hanheldpos.model.product.buy_x_get_y.GroupBuyXGetY
import com.hanheldpos.model.product.buy_x_get_y.ItemBuyXGetYGroup
import com.hanheldpos.model.product.buy_x_get_y.ProductTypeTab
import com.hanheldpos.model.product.combo.ItemActionType
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.screens.cart.CurCartData

class BuyXGetYGroupAdapter(
    private val listener: BuyXGetYItemListener,
) : BaseBindingListAdapter<ItemBuyXGetYGroup>(DiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_buy_x_get_y_group
    }

    data class SelectedItem(var value: Int = 0)

    private val selectedItem: SelectedItem = SelectedItem(0)

    override fun submitList(list: MutableList<ItemBuyXGetYGroup>?) {
        super.submitList(list)
    }

    override fun onBindViewHolder(
        holder: BaseBindingViewHolder<ItemBuyXGetYGroup>,
        position: Int
    ) {
        val itemBuyXGetYGroup = getItem(position)

        val binding = holder.binding as ItemBuyXGetYGroupBinding
        binding.position = (position + 1).toString()
        binding.name = itemBuyXGetYGroup.getGroupName()
        binding.item = itemBuyXGetYGroup

        if (position == 1) {
            binding.linearProgressContainer.visibility = View.GONE
            binding.topSpacer.visibility = View.GONE
        }

        // Check your turn
        var positionFocus: Int = -1
        run checkFocus@{
            currentList.forEachIndexed { index, itemBuyXGetYGroup ->
                if (!itemBuyXGetYGroup.isMaxItemSelected()) {
                    positionFocus = index
                    if (position == 1 && positionFocus == position) {
                        if (itemBuyXGetYGroup.isApplyToEntireOrder == false)
                            binding.topSpacer.visibility = View.VISIBLE
                    }
                    if (itemBuyXGetYGroup.groupBuyXGetY.condition is CustomerBuys) {
                        val result =
                            (itemBuyXGetYGroup.groupBuyXGetY.condition as CustomerBuys).getProgressValue(
                                CurCartData.cartModel!!.total(),
                                CurCartData.cartModel!!.getBuyXGetYQuantity(itemBuyXGetYGroup.groupBuyXGetY.parentDisc_Id)
                            )
                        binding.linearProgress.linearProgressIndicator.progress = result
                    }
                    return@checkFocus
                } else if (itemBuyXGetYGroup.isApplyToEntireOrder == true) {
                    if (itemBuyXGetYGroup.isBuyComplete == true && position == 0) {
                        val result =
                            (itemBuyXGetYGroup.groupBuyXGetY.condition as CustomerBuys).getProgressValue(
                                CurCartData.cartModel!!.total(),
                                CurCartData.cartModel!!.getBuyXGetYQuantity(itemBuyXGetYGroup.groupBuyXGetY.parentDisc_Id)
                            )
                        binding.linearProgress.linearProgressIndicator.progress = result
                    }
                }
            }
        }

        // Ẩn thông tin combo khi chưa tới lượt
        if (positionFocus == position) {
            selectedItem.value = position
            itemBuyXGetYGroup.isFocused = true
        } else itemBuyXGetYGroup.isFocused = false

        val buyXGetYItemPickerAdapter = BuyXGetYItemPickerAdapter(
            listener = object : BaseItemClickListener<BaseProductInCart> {
                override fun onItemClick(adapterPosition: Int, item: BaseProductInCart) {
                    item.quantity = 1
                    listener.onProductSelect(
                        1,
                        itemBuyXGetYGroup.groupBuyXGetY,
                        item,
                        ItemActionType.Add,
                    )
                }
            }
        )
        binding.itemForSelectAdapter.apply {
            adapter = buyXGetYItemPickerAdapter
        }
        binding.itemForSelectAdapter.itemAnimator = null

        // Set group that had already been selected
        val buyXGetYItemChosenAdapter = BuyXGetYItemChosenAdapter(
            listener = object : BuyXGetYItemChosenAdapter.ComboItemChosenListener {
                override fun onComboItemChoose(
                    action: ItemActionType,
                    item: BaseProductInCart
                ) {
                    listener.onProductSelect(
                        itemBuyXGetYGroup.requireQuantity(),
                        itemBuyXGetYGroup.groupBuyXGetY,
                        item,
                        action,
                    )
                }
            }
        ).also {
            it.submitList(itemBuyXGetYGroup.groupBuyXGetY.productList)
        }
        binding.itemSelectedAdapter.apply {
            adapter = buyXGetYItemChosenAdapter
        }
        binding.itemSelectedAdapter.itemAnimator = null

        //Set up tab layout
        val listTemp = getProductTypeTab(itemBuyXGetYGroup.listApplyTo?.toList())?.toList()
            ?: listOf()
        binding.tabDiscountType.removeAllTabs()
        listTemp.forEach { tab ->
            binding.tabDiscountType.addTab(
                binding.tabDiscountType.newTab().setText(tab.title)
            )
        }

        val tabSelected = fun(tab: TabLayout.Tab?) {
            tab ?: return
            buyXGetYItemPickerAdapter.submitList(
                itemBuyXGetYGroup.groupListBaseProduct?.get(
                    tab.position
                )
            )
        }

        binding.tabDiscountType.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tabSelected(tab)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                tabSelected(tab)
            }
        })

        binding.tabDiscountType.getTabAt(0)?.select()

    }

    private fun getProductTypeTab(listApplyTo: List<Product>?): MutableList<ProductTypeTab>? {
        return listApplyTo?.map { list -> ProductTypeTab(title = list.Name1, _id = list._id) }
            ?.toMutableList()
    }

    interface BuyXGetYItemListener {
        fun onProductSelect(
            maxQuantity: Int,
            group: GroupBuyXGetY,
            item: BaseProductInCart,
            actionType: ItemActionType,
        )
    }

    private class DiffCallback : DiffUtil.ItemCallback<ItemBuyXGetYGroup>() {
        override fun areItemsTheSame(
            oldItem: ItemBuyXGetYGroup,
            newItem: ItemBuyXGetYGroup
        ): Boolean {
            return oldItem.groupBuyXGetY == newItem.groupBuyXGetY
        }

        override fun areContentsTheSame(
            oldItem: ItemBuyXGetYGroup,
            newItem: ItemBuyXGetYGroup
        ): Boolean {
            return oldItem.groupBuyXGetY == newItem.groupBuyXGetY
        }
    }
}