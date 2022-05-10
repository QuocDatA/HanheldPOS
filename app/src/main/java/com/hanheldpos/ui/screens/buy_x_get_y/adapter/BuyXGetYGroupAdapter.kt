package com.hanheldpos.ui.screens.buy_x_get_y.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.discount.CustomerBuys
import com.hanheldpos.data.api.pojo.fee.CustomerGets
import com.hanheldpos.databinding.ItemBuyXGetYGroupBinding
import com.hanheldpos.model.buy_x_get_y.ItemBuyXGetYGroup
import com.hanheldpos.model.cart.Regular
import com.hanheldpos.model.buy_x_get_y.GroupBuyXGetY
import com.hanheldpos.model.combo.ItemActionType
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.screens.cart.CurCartData

class BuyXGetYGroupAdapter(private val listener: BuyXGetYItemListener) : BaseBindingListAdapter<ItemBuyXGetYGroup>(DiffCallback()) {

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
        // Check your turn
        var positionFocus: Int = -1
        run checkFocus@{
            currentList.forEachIndexed { index, itemBuyXGetYGroup ->
                if (!itemBuyXGetYGroup.isMaxItemSelected()) {
                    positionFocus = index
                    return@checkFocus
                }
            }
        }
        val itemBuyXGetYGroup = getItem(position)
        // Ẩn thông tin combo khi chưa tới lượt
        if (positionFocus == position) {
            selectedItem.value = position
            itemBuyXGetYGroup.isFocused = true
        } else itemBuyXGetYGroup.isFocused = false

        val binding = holder.binding as ItemBuyXGetYGroupBinding
        binding.position = (position + 1).toString()
        binding.name = itemBuyXGetYGroup.getGroupName()
        binding.item = itemBuyXGetYGroup

        val groupRegular: MutableList<Regular> = mutableListOf()
        val groupChosen : MutableList<Regular> = mutableListOf()
        itemBuyXGetYGroup.groupBuyXGetY.productList.forEach { basePro -> groupChosen.add(basePro as Regular) }


        val conditionCustomer: Any
        if (itemBuyXGetYGroup.groupBuyXGetY.condition is CustomerBuys) {
            conditionCustomer = itemBuyXGetYGroup.groupBuyXGetY.condition as CustomerBuys
            groupRegular.addAll(
                itemBuyXGetYGroup.getProductListApplyToBuyXGetY(
                    conditionCustomer.ListApplyTo,
                    CurCartData.cartModel?.diningOption!!
                ).toMutableList()
            )

        } else {
            conditionCustomer = itemBuyXGetYGroup.groupBuyXGetY.condition as CustomerGets
            groupRegular.addAll(
                itemBuyXGetYGroup.getProductListApplyToBuyXGetY(
                    conditionCustomer.ListApplyTo,
                    CurCartData.cartModel?.diningOption!!
                ).toMutableList()
            )
        }

        binding.itemForSelectAdapter.apply {
            adapter = BuyXGetYItemPickerAdapter(
//                proOriginal = itemBuyXGetYGroup.groupBuyXGetY.productList.first(),
//                groupBundle = itemComboGroup.groupBundle,
//                productChosen = groupRegular,
                listener = object: BaseItemClickListener<Regular> {
                    override fun onItemClick(adapterPosition: Int, item: Regular) {
                        item.quantity = 1
                        listener.onProductSelect(1, itemBuyXGetYGroup.groupBuyXGetY, item, ItemActionType.Add,)
                    }
                }
            ).apply {
                submitList(groupRegular)
            }
        }
        binding.itemSelectedAdapter.apply {
            adapter = BuyXGetYItemChosenAdapter(
//                listener = object : ComboItemChosenAdapter.ComboItemChosenListener {
//                    override fun onComboItemChoose(
//                        action: ItemActionType,
//                        item: Regular
//                    ) {
//                        listener.onProductSelect(
//                            itemComboGroup.requireQuantity(),
//                            itemComboGroup,
//                            item,
//                            action
//                        )
//                    }
//                }
            ).apply {
                submitList(groupChosen)
            }
        }
    }

    interface BuyXGetYItemListener {
        fun onProductSelect(
            maxQuantity: Int,
            group: GroupBuyXGetY,
            item: Regular,
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