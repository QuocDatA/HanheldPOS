package com.hanheldpos.ui.screens.home.order.combo.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.databinding.ItemComboPickedBinding
import com.hanheldpos.databinding.ItemOrderProductBinding
import com.hanheldpos.model.home.order.combo.ComboItemActionType
import com.hanheldpos.model.home.order.menu.ComboPickedItemViewModel
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder

class ComboItemAdapter(
    private val modeViewType: ComboItemViewType?= ComboItemViewType.ForChoose,
    private val listener: ComboItemListener
) : BaseBindingListAdapter<ComboPickedItemViewModel>(DiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        return when(modeViewType){
            ComboItemViewType.ForChoose->{
                R.layout.item_order_product
            }
            else->{
                R.layout.item_combo_picked
            }
        } ;
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseBindingViewHolder<ComboPickedItemViewModel> {
        DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            viewType,
            parent, false
        ).also {

            when(modeViewType){
                ComboItemViewType.ForChoose->{

                    Log.d("OrderProductAdapter","RecycleView Height:" + parent.height);
                    val height = parent.resources.getDimension(R.dimen._75sdp);
                    val params : FrameLayout.LayoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,height.toInt());
                    (it as ItemOrderProductBinding).layoutMain.layoutParams = params;
                }
            }

            return BaseBindingViewHolder(it)
        }
    }

    override fun onBindViewHolder(
        holder: BaseBindingViewHolder<ComboPickedItemViewModel>,
        position: Int
    ) {
        val item = getItem(position);
        when(modeViewType){
            ComboItemViewType.ForChoose->{
                val binding = holder.binding as ItemOrderProductBinding;
                binding.item = item.selectedComboItem?.productOrderItem;
                binding.root.setOnClickListener {
                    listener.onComboItemChoose(action = ComboItemActionType.Add,item);
                }
                binding.isChosen=item.isChosen;
            }
            ComboItemViewType.Chosen->{
                val binding = holder.binding as ItemComboPickedBinding;
                binding.item = item;
                binding.itemComboModify.setOnClickListener {
                    listener.onComboItemChoose(action = ComboItemActionType.Modify,item);
                }
                binding.itemComboRemove.setOnClickListener {
                    listener.onComboItemChoose(action = ComboItemActionType.Remove,item);
                }
            }
        }

    }

    enum class ComboItemViewType(val value: Int) {
        ForChoose(1),
        Chosen(2),
    }

    interface ComboItemListener {
        fun onComboItemChoose(action: ComboItemActionType, item : ComboPickedItemViewModel)
    }

    class DiffCallback : DiffUtil.ItemCallback<ComboPickedItemViewModel>() {
        override fun areItemsTheSame(
            oldItem: ComboPickedItemViewModel,
            newItem: ComboPickedItemViewModel
        ): Boolean {
            return oldItem == newItem;
        }

        override fun areContentsTheSame(
            oldItem: ComboPickedItemViewModel,
            newItem: ComboPickedItemViewModel
        ): Boolean {
            return false
        }

    }
}