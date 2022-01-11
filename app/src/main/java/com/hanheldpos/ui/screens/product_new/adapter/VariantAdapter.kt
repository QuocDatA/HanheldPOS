package com.hanheldpos.ui.screens.product_new.adapter

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.data.PosConst
import com.hanheldpos.data.api.pojo.product.VariantsGroup
import com.hanheldpos.databinding.ItemTemporaryVariantSauceBinding
import com.hanheldpos.databinding.ItemVariantButtonStyleBinding
import com.hanheldpos.databinding.ItemVariantRadioStyleBinding
import com.hanheldpos.model.product.VariantGroupType
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.utils.constants.Const

class VariantAdapter(
    private val variantViewType: VariantGroupType? = VariantGroupType.ButtonStyle,
    private val listener: BaseItemClickListener<VariantsGroup.OptionValueVariantsGroup>
) : BaseBindingListAdapter<VariantsGroup.OptionValueVariantsGroup>(DiffCallback()) {

    var selectedItem: Int = 0

    override fun getItemViewType(position: Int): Int {
        return when(variantViewType) {
            VariantGroupType.ButtonStyle-> R.layout.item_variant_button_style;
            VariantGroupType.RadioStyle-> R.layout.item_variant_radio_style;
            else -> R.layout.item_variant_button_style;
        } ;
    }

    override fun submitList(list: MutableList<VariantsGroup.OptionValueVariantsGroup>?) {
        selectedItem = 0
        super.submitList(list)
    }

    override fun onBindViewHolder(
        holder: BaseBindingViewHolder<VariantsGroup.OptionValueVariantsGroup>,
        position: Int
    ) {
        val item = getItem(position);
        holder.bindItem(item);
        when(variantViewType){
            VariantGroupType.ButtonStyle -> {
                val binding = (holder.binding as ItemVariantButtonStyleBinding);
                binding.optionBtn.apply {
                    if (item.Visible == PosConst.IN_VISIBLE) {
                        if(selectedItem == position) {
                            selectedItem = if ((position + 1) >= itemCount) -1 else position + 1
                        }
                        binding.optionBtn.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.color_8
                            )
                        )
                        return;
                    }
                    setOnClickListener {
                        notifyItemChanged(selectedItem)
                        selectedItem = holder.absoluteAdapterPosition;
                        notifyItemChanged(holder.absoluteAdapterPosition)
                    }
                    if (selectedItem == position) {
                        isChecked = true
                        binding.optionBtn.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.color_2
                            )
                        )
                        listener.onItemClick(position, item);
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
            VariantGroupType.RadioStyle -> {
                val binding = holder.binding as ItemVariantRadioStyleBinding;
                binding.btnProductItem.apply {
                    if (item.Visible == PosConst.IN_VISIBLE) {
                        if(selectedItem == position) {
                            selectedItem = if ((position + 1) >= itemCount) -1 else position + 1
                        }
                        binding.btnProductItem.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.color_8
                            )
                        )
                        return;
                    }
                    setOnClickListener {
                        notifyItemChanged(selectedItem);
                        selectedItem = holder.absoluteAdapterPosition;
                        notifyItemChanged(holder.absoluteAdapterPosition);

                    }
                    if (selectedItem == position) {
                        isChecked = true
                        listener.onItemClick(position, item);
                    } else {
                        isChecked = false;
                    }

                }
            }
        }

    }

    private class DiffCallback : DiffUtil.ItemCallback<VariantsGroup.OptionValueVariantsGroup>() {
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