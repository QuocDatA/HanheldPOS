package com.hanheldpos.ui.screens.product.adapter.variant

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.diadiem.pos_config.utils.Const
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.order.menu.GroupItem
import com.hanheldpos.databinding.ItemContainerVarientBinding
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.screens.product.adapter.GridSpacingItemDecoration

class ContainerVariantAdapter(
    private val itemSelected : GroupItem? = null,
    private val listener: BaseItemClickListener<String?>
) : BaseBindingListAdapter<VariantHeader>(DiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_container_varient;
    }

    private var level : Int = 0;
    private var variantSelected : MutableMap<Int,String>? = null;

    override fun submitList(list: MutableList<VariantHeader>?) {
        this.level = 0;
        variantSelected = mutableMapOf();
        super.submitList(list)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: BaseBindingViewHolder<VariantHeader>, position: Int) {
        val item = getItem(position);
        holder.bindItem(item);
        val binding = (holder.binding as ItemContainerVarientBinding);
        val itemLevel = ++this.level // Use level to check ordinal of variant string
        VariantAdapter(
            level = itemLevel,
            listener = object : BaseItemClickListener<VariantLayoutItem> {
            override fun onItemClick(adapterPosition: Int, item: VariantLayoutItem) {
                variantSelected?.set(itemLevel, item.name);
                listener.onItemClick(adapterPosition, getGroupItemFromVariantSelected());
            }
        }).also { variantAdapter ->
            binding.containerVariantItem.adapter = variantAdapter;
            binding.containerVariantItem.addItemDecoration(GridSpacingItemDecoration(2, 20, false))

            /**
             * Fix scroll when change type variant
             */
            binding.containerVariantItem.setHasFixedSize(true);

            val list = item.childList?.toMutableList();
            variantAdapter.submitList(list);
            /**
             *  Restore option choosed
            */
            itemSelected?.groupName?.split("•")?.get(itemLevel-1).let { name ->
                run lit@{
                    list?.forEach {
                        if (it.name == name){
                            variantAdapter.selectedItem.value = list.indexOf(it);
                            return@lit;
                        }
                    }
                }
            }
            variantAdapter.notifyDataSetChanged();
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<VariantHeader>() {

        override fun areItemsTheSame(oldItem: VariantHeader, newItem: VariantHeader): Boolean {
            return oldItem == newItem;
        }

        override fun areContentsTheSame(oldItem: VariantHeader, newItem: VariantHeader): Boolean {
            return oldItem == newItem;
        }

    }

    private fun getGroupItemFromVariantSelected() : String?{
        val rs : MutableList<String> = mutableListOf();
        for (i in 1..level){
            if (variantSelected != null && variantSelected?.contains(i) == true)
            {
                variantSelected!![i]?.let { rs.add(it) }
            }
            else return null;
        }
        return rs.joinToString(Const.SymBol.VariantSeparator)
    }
}