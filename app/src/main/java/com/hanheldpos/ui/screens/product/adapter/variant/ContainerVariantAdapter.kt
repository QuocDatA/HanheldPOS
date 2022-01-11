package com.hanheldpos.ui.screens.product.adapter.variant

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.product.VariantsGroup
import com.hanheldpos.databinding.ItemProductGroupVariantBinding
import com.hanheldpos.model.cart.VariantCart
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.screens.product.adapter.GridSpacingItemDecoration

class ContainerVariantAdapter(
    private val listener: BaseItemClickListener<VariantsGroup.OptionValueVariantsGroup>
) : BaseBindingListAdapter<VariantsGroup>(DiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_product_group_variant;
    }
    private var variantSelected : MutableMap<Int,String>? = null;
    var itemSelected : List<VariantCart>? = null

    override fun submitList(list: MutableList<VariantsGroup>?) {
        variantSelected = mutableMapOf();
        super.submitList(list)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: BaseBindingViewHolder<VariantsGroup>, position: Int) {
        val item = getItem(position);
        holder.bindItem(item);
        val binding = (holder.binding as ItemProductGroupVariantBinding);
        VariantAdapter(
            listener = object : BaseItemClickListener<VariantsGroup.OptionValueVariantsGroup> {
            override fun onItemClick(adapterPosition: Int, item: VariantsGroup.OptionValueVariantsGroup) {
//                variantSelected?.set(itemLevel, item.name);
                listener.onItemClick(adapterPosition, item);
            }
        }).also { variantAdapter ->
            binding.containerVariantItem.adapter = variantAdapter;
            if (binding.containerVariantItem.itemDecorationCount == 0){
                binding.containerVariantItem.addItemDecoration(GridSpacingItemDecoration(2, 20, false))
            }
            /**
             * Fix scroll when change type variant
             */
            binding.containerVariantItem.setHasFixedSize(true);

            val list = item.subOptionValueList()?.toMutableList();
            variantAdapter.submitList(list);
            /**
             *  Restore option choosed
            */
            if(itemSelected?.size?:0 > position)
            itemSelected?.get(position).let { variant ->
                run lit@{
                    list?.forEach {
                        if (it.Id == variant?.Id){
                            variantAdapter.selectedItem.value = list.indexOf(it);
                            return@lit;
                        }
                    }
                }
            }
            variantAdapter.notifyDataSetChanged();
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<VariantsGroup>() {

        override fun areItemsTheSame(oldItem: VariantsGroup, newItem: VariantsGroup): Boolean {
            return oldItem == newItem;
        }

        override fun areContentsTheSame(oldItem: VariantsGroup, newItem: VariantsGroup): Boolean {
            return false;
        }

    }


}