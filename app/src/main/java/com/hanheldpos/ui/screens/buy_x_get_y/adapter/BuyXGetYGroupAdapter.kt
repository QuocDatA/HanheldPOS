package com.hanheldpos.ui.screens.buy_x_get_y.adapter

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.DiffUtil
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.product.Product
import com.hanheldpos.databinding.ItemBuyXGetYGroupBinding
import com.hanheldpos.model.buy_x_get_y.GroupBuyXGetY
import com.hanheldpos.model.buy_x_get_y.ItemBuyXGetYGroup
import com.hanheldpos.model.buy_x_get_y.ProductTypeTab
import com.hanheldpos.model.cart.Regular
import com.hanheldpos.model.combo.ItemActionType
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.screens.discount.adapter.OptionsPagerAdapter

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

        // Set group that had already been selected
        val groupChosen: MutableList<Regular> = mutableListOf()
        itemBuyXGetYGroup.groupBuyXGetY.productList.forEach { basePro -> groupChosen.add(basePro as Regular) }

        if(itemBuyXGetYGroup.isApplyToEntireOrder == false) {
            val buyXGetYItemPickerAdapter = BuyXGetYItemPickerAdapter(
                listener = object : BaseItemClickListener<Regular> {
                    override fun onItemClick(adapterPosition: Int, item: Regular) {
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

            val buyXGetYItemChosenAdapter = BuyXGetYItemChosenAdapter(
                listener = object : BuyXGetYItemChosenAdapter.ComboItemChosenListener {
                    override fun onComboItemChoose(action: ItemActionType, item: Regular) {
                        listener.onProductSelect(
                            itemBuyXGetYGroup.requireQuantity(),
                            itemBuyXGetYGroup.groupBuyXGetY,
                            item,
                            action,
                        )
                    }
                }
            ).also { it.submitList(groupChosen) }
            binding.itemSelectedAdapter.apply {
                adapter = buyXGetYItemChosenAdapter
            }

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
                    itemBuyXGetYGroup.groupListRegular?.get(
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
        } else {

        }

    }

    private fun getProductTypeTab(listApplyTo: List<Product>?): MutableList<ProductTypeTab>? {
        return listApplyTo?.map { list -> ProductTypeTab(title = list.Name1, _id = list._id) }
            ?.toMutableList()
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