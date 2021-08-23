package com.hanheldpos.ui.screens.home.table.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.table.FloorTableItem
import com.hanheldpos.databinding.ItemTableBinding
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.base.adapter.BaseItemClickListener

class TableAdapter(
    private val listener: BaseItemClickListener<FloorTableItem>
) : BaseBindingListAdapter<FloorTableItem>(DiffCallBack(),listener) {

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_table;
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseBindingViewHolder<FloorTableItem> {
        DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            viewType,
            parent, false
        ).also {
            Log.d("TableAdapter","RecycleView Height:" + parent.height);
            val height = ((parent.height) / 5 ) - parent.resources.getDimension(R.dimen._6sdp);
            val params : FrameLayout.LayoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,height.toInt());
            (it as ItemTableBinding).layoutMain.layoutParams = params;
            return BaseBindingViewHolder(it, listener)
        }
    }

    private class DiffCallBack : DiffUtil.ItemCallback<FloorTableItem>(){
        override fun areItemsTheSame(oldItem: FloorTableItem, newItem: FloorTableItem): Boolean {
            return oldItem == newItem;
        }

        override fun areContentsTheSame(oldItem: FloorTableItem, newItem: FloorTableItem): Boolean {
            return oldItem == newItem;
        }

    }
}