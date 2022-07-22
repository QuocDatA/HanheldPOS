package com.hanheldpos.ui.screens.product.regular.adapter

import android.annotation.SuppressLint
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.product.VariantsGroup
import com.hanheldpos.databinding.ItemProductGroupVariantBinding
import com.hanheldpos.model.cart.VariantCart
import com.hanheldpos.model.product.VariantGroupType
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.base.adapter.GridSpacingItemDecoration

class GroupVariantAdapter(
    private val listener: BaseItemClickListener<VariantsGroup.OptionValueVariantsGroup>
    ) :
    BaseBindingListAdapter<VariantsGroup>(DiffCallBack()) {

    var itemSelected: List<VariantCart>? = null

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_product_group_variant
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(
        holder: BaseBindingViewHolder<VariantsGroup>,
        position: Int
    ) {
        val item = getItem(position)
        holder.bindItem(item)
        val binding = (holder.binding as ItemProductGroupVariantBinding)
        VariantAdapter(
            variantViewType = VariantGroupType.fromInt(item.VariantOptionType ?: 0),
            listener = object : BaseItemClickListener<VariantsGroup.OptionValueVariantsGroup> {
                override fun onItemClick(
                    adapterPosition: Int,
                    item: VariantsGroup.OptionValueVariantsGroup
                ) {
                    listener.onItemClick(adapterPosition, item)
                }
            },
        ).also { variantAdapter ->
            binding.containerVariantItem.adapter = variantAdapter
            binding.containerVariantItem.itemAnimator = null

            /*
            * Setup layout manager
            * */
            when (VariantGroupType.fromInt(item.VariantOptionType ?: 0)) {
                VariantGroupType.ButtonStyle -> {
                    binding.containerVariantItem.layoutManager =
                        GridLayoutManager(holder.binding.root.context, 2)
                    if (binding.containerVariantItem.itemDecorationCount == 0) {
                        binding.containerVariantItem.addItemDecoration(
                            GridSpacingItemDecoration(
                                2,
                                20,
                                false
                            )
                        )
                    }
                }
                VariantGroupType.RadioStyle -> {
                    binding.containerVariantItem.layoutManager =
                        LinearLayoutManager(holder.binding.root.context)
                    if (binding.containerVariantItem.itemDecorationCount == 0) {
                        binding.containerVariantItem.addItemDecoration(
                            DividerItemDecoration(
                                holder.binding.root.context,
                                LinearLayoutManager.VERTICAL
                            ).apply {
                                setDrawable(
                                    ContextCompat.getDrawable(
                                        holder.binding.root.context,
                                        R.drawable.divider_vertical
                                    )!!
                                )
                            }
                        )
                    }
                }

                else -> {
                    binding.containerVariantItem.layoutManager =
                        GridLayoutManager(holder.binding.root.context, 2)
                    if (binding.containerVariantItem.itemDecorationCount == 0) {
                        binding.containerVariantItem.addItemDecoration(
                            GridSpacingItemDecoration(
                                2,
                                20,
                                false
                            )
                        )
                    }
                }
            }

            /**
             * Fix scroll when change type variant
             */
            binding.containerVariantItem.setHasFixedSize(true)

            val list = item.subOptionValueList()?.toMutableList()
            variantAdapter.submitList(list)
            /**
             *  Restore option chosen
             */
            if (itemSelected?.size ?: 0 > position)
                itemSelected?.get(position).let { variant ->
                    run lit@{
                        list?.forEach {
                            if (it.Id == variant?.Id) {
                                variantAdapter.selectedItem = list.indexOf(it)
                                return@lit
                            }
                        }
                    }
                }
            variantAdapter.notifyDataSetChanged()
        }
    }


    private class DiffCallBack : DiffUtil.ItemCallback<VariantsGroup>() {
        override fun areItemsTheSame(
            oldItem: VariantsGroup,
            newItem: VariantsGroup
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: VariantsGroup,
            newItem: VariantsGroup
        ): Boolean {
            return false
        }

    }
}