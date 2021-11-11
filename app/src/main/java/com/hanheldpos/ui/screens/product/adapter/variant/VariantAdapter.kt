package com.hanheldpos.ui.screens.product.adapter.variant

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.product.VariantsGroup
import com.hanheldpos.databinding.ItemVariantBinding
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.base.adapter.BaseItemClickListener

class VariantAdapter(
    private val listener : BaseItemClickListener<VariantsGroup.OptionValueVariantsGroup>
) : BaseBindingListAdapter<VariantsGroup.OptionValueVariantsGroup>(DiffCallback(),listener) {

    data class SelectedItem(var value: Int = 0)

    val selectedItem: SelectedItem = SelectedItem(0)

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_variant;
    }

    override fun submitList(list: MutableList<VariantsGroup.OptionValueVariantsGroup>?) {
        selectedItem.value = 0
        super.submitList(list)
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<VariantsGroup.OptionValueVariantsGroup>, position: Int) {
        val item = getItem(position);
        holder.bindItem(item);
        val binding  = (holder.binding as ItemVariantBinding);
        binding.optionBtn.apply {
            setOnClickListener {
                notifyItemChanged(selectedItem.value)
                selectedItem.value = position;
                notifyItemChanged(position)
            }
            if (selectedItem.value == position) {
                isChecked = true
                binding.optionBtn.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.color_2
                    )
                )
                listener.onItemClick(position,item);
            } else {
                isChecked = false
                binding.optionBtn.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.color_4
                    )
                )
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<VariantsGroup.OptionValueVariantsGroup>() {
        override fun areItemsTheSame(
            oldItem: VariantsGroup.OptionValueVariantsGroup,
            newItem: VariantsGroup.OptionValueVariantsGroup
        ): Boolean {
            return oldItem.Id == newItem.Id;
        }

        override fun areContentsTheSame(
            oldItem: VariantsGroup.OptionValueVariantsGroup,
            newItem: VariantsGroup.OptionValueVariantsGroup
        ): Boolean {
            return true;
        }

    }
}